package ba.unsa.etf.nwt.adopt_service.service;

import ba.unsa.etf.nwt.adopt_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.adopt_service.model.AdoptionRequest;
import ba.unsa.etf.nwt.adopt_service.rabbitmq.MessagingConfig;
import ba.unsa.etf.nwt.adopt_service.rabbitmq.NotificationAdoptServiceMessage;
import ba.unsa.etf.nwt.adopt_service.repository.AdoptionRequestRepository;
import ba.unsa.etf.nwt.adopt_service.request.PetForAdoptRequest;
import ba.unsa.etf.nwt.adopt_service.response.ResponseMessage;
import ba.unsa.etf.nwt.adopt_service.security.CurrentUser;
import ba.unsa.etf.nwt.adopt_service.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AdoptionRequestService {
    private final AdoptionRequestRepository adoptionRequestRepository;
    private final CommunicationsService communicationsService;

    private List<AdoptionRequest> allRequestsForDelete;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public List<AdoptionRequest> getAdoptionRequest() {
        return adoptionRequestRepository.findAll();
    }

    public ResponseMessage addAdoptionRequest(String token, Long id, PetForAdoptRequest petForAdoptRequest, @CurrentUser UserPrincipal currentUser) {

        AdoptionRequest adoptionRequest = new AdoptionRequest();

        RestTemplate restTemplate = new RestTemplate();
        adoptionRequest.setUserID(currentUser.getId());
        adoptionRequest.setPetID(id);
        adoptionRequest.setApproved(false);
        adoptionRequest.setMessage(petForAdoptRequest.getMessage());
        adoptionRequestRepository.save(adoptionRequest);

        try {
            //sinhrono
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<String> entityReq = new HttpEntity<>("", headers);

            ResponseEntity<ResponseMessage> responseMessage = restTemplate.exchange(communicationsService.getUri("notification_service")
                            + "/notifications/add/adopt-request/" + adoptionRequest.getUserID() + "/" + adoptionRequest.getId(),
                            HttpMethod.GET ,entityReq, ResponseMessage.class);

            System.out.println(responseMessage.getBody().getMessage());
        } catch (Exception ue){
                throw new ResourceNotFoundException("Can't connect to notification_service!");
        }

        return new ResponseMessage(true, HttpStatus.OK, "Request to adopt a pet with ID=" + adoptionRequest.getPetID() + " added successfully!");
    }

    public ResponseMessage addAdoptionRequestLocal(AdoptionRequest adoptionRequest) {
        AdoptionRequest novi = new AdoptionRequest();
        novi.setUserID(adoptionRequest.getUserID());
        novi.setPetID(adoptionRequest.getPetID());
        novi.setMessage(adoptionRequest.getMessage());
        adoptionRequestRepository.save(novi);
        return new ResponseMessage(true, HttpStatus.OK, "Request to adopt a pet with ID=" + adoptionRequest.getPetID() + " added successfully!");

    }

    public List<AdoptionRequest> getAdoptionRequestByUserID(Long userID) {
        return adoptionRequestRepository
                .findAll()
                .stream()
                .filter(n -> n.getUserID().equals(userID))
                .collect(Collectors.toList());
    }

    public List<AdoptionRequest> getAdoptionRequestByPetID(Long petID) {
        return adoptionRequestRepository
                .findAll()
                .stream()
                .filter(n -> n.getPetID().equals(petID))
                .collect(Collectors.toList());
    }

    public List<AdoptionRequest> getApprovedAdoptionRequests() {
        return adoptionRequestRepository
                .findAll()
                .stream()
                .filter(n -> n.isApproved())
                .collect(Collectors.toList());
    }

    public List<AdoptionRequest> getNotApprovedAdoptionRequests() {
        return adoptionRequestRepository
                .findAll()
                .stream()
                .filter(n -> (!n.isApproved()))
                .collect(Collectors.toList());
    }

    public ResponseMessage deleteAdoptionRequestByID(String token, Long id) {
        try {
            AdoptionRequest adoptionRequest = adoptionRequestRepository
                    .findAll()
                    .stream()
                    .filter(n -> n.getId().equals(id))
                    .collect(Collectors.toList()).get(0);

            RestTemplate restTemplate = new RestTemplate();

            if(adoptionRequest.isApproved()) {
                try {

                    //sinhrono
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", token);

                    HttpEntity<String> entityReq = new HttpEntity<>("", headers);

                    ResponseEntity<ResponseMessage> responseMessage = restTemplate.exchange(communicationsService.getUri("pet_category_service")
                                    + "/pet?id=" + adoptionRequest.getPetID(),
                            HttpMethod.DELETE, entityReq, ResponseMessage.class);

                    System.out.println(responseMessage.getBody().getMessage());
                } catch (Exception ue) {
                    throw new ResourceNotFoundException("Can't connect to pet_category_service and delete a pet!");
                }

                communicationsService.deleteForAdd(token, adoptionRequest.getPetID());

            } else {

                //send message to notification_service
                NotificationAdoptServiceMessage notificationAdoptServiceMessage = new NotificationAdoptServiceMessage(adoptionRequest.getUserID(),
                        adoptionRequest.getId(), false, false);
                rabbitTemplate.convertAndSend(MessagingConfig.ADOPT_NOTIFICATION_SERVICE_EXCHANGE,
                        MessagingConfig.ADOPT_NOTIFICATION_SERVICE_ROUTING_KEY, notificationAdoptServiceMessage);
            }

            adoptionRequestRepository.delete(adoptionRequest);

            if (adoptionRequestRepository.findById(id) != null)
                return new ResponseMessage(true, HttpStatus.OK, "Adoption request with id=" + id + " deleted successfully!");
            return new ResponseMessage(false, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server or database error!");
        } catch (Exception e) {
            return new ResponseMessage(false, HttpStatus.NOT_FOUND, "There is no adoption request with id=" + id + "!");
        }
    }

    public ResponseMessage setNotApproved(String token, Long id){
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found!"));

        //send message to notification_service
        NotificationAdoptServiceMessage notificationAdoptServiceMessage = new NotificationAdoptServiceMessage(adoptionRequest.getUserID(),
                adoptionRequest.getId(), false, false);
        rabbitTemplate.convertAndSend(MessagingConfig.ADOPT_NOTIFICATION_SERVICE_EXCHANGE,
                MessagingConfig.ADOPT_NOTIFICATION_SERVICE_ROUTING_KEY, notificationAdoptServiceMessage);

        adoptionRequest.setApproved(false);
        adoptionRequestRepository.save(adoptionRequest);

        return new ResponseMessage(true, HttpStatus.OK, "You didn't approve this request!");
    }

    public ResponseMessage setApproved(String token, Long id){
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found!"));

        //poziva se ruta iz pet servisa da se i tamo odobri..

        RestTemplate restTemplate = new RestTemplate();
        try {

            //sinhrono
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<String> entityReq = new HttpEntity<>("", headers);

            //brise se pet na pet servisu jer je adoptan
            ResponseEntity<ResponseMessage> responseMessage = restTemplate.exchange(communicationsService.getUri("pet_category_service")
                            + "/pets/approve/1/" + adoptionRequest.getPetID(),
                    HttpMethod.PUT ,entityReq, ResponseMessage.class);

            System.out.println(responseMessage.getBody().getMessage());
        } catch (Exception ue){
            throw new ResourceNotFoundException("Can't connect to pet_category_service and approve a pet!");
        }

        //salje se notifikacija korisniku

        //send message to notification_service
        NotificationAdoptServiceMessage notificationAdoptServiceMessage = new NotificationAdoptServiceMessage(adoptionRequest.getUserID(),
                adoptionRequest.getId(), true, false);
        rabbitTemplate.convertAndSend(MessagingConfig.ADOPT_NOTIFICATION_SERVICE_EXCHANGE,
                MessagingConfig.ADOPT_NOTIFICATION_SERVICE_ROUTING_KEY, notificationAdoptServiceMessage);


        adoptionRequest.setApproved(true);
        adoptionRequestRepository.save(adoptionRequest);

        //kada se odobri neki request za nekog peta, svi ostali zahtjevi za taj petID se brisu
        deleteOtherRequests(token, adoptionRequest.getPetID());

        return new ResponseMessage(true, HttpStatus.OK, "This request has been approved!");
    }

    public AdoptionRequest getAdoptionRequestbyId(Long id){
        return adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found!"));
    }

    public void deleteOtherRequests(String token, Long petID){

        List<AdoptionRequest> adoptionRequests =
                adoptionRequestRepository.findAllByApprovedAndPetID(false, petID);

        for(AdoptionRequest adoptionRequest : adoptionRequests){
            //send message to notification_service
            NotificationAdoptServiceMessage notificationAdoptServiceMessage = new NotificationAdoptServiceMessage(adoptionRequest.getUserID(),
                    adoptionRequest.getId(), false, false);
            rabbitTemplate.convertAndSend(MessagingConfig.ADOPT_NOTIFICATION_SERVICE_EXCHANGE,
                    MessagingConfig.ADOPT_NOTIFICATION_SERVICE_ROUTING_KEY, notificationAdoptServiceMessage);

            adoptionRequestRepository.deleteById(adoptionRequest.getId());
        }

    }

    public void findAndDeleteAdoptionRequest(Long id){
        List<AdoptionRequest> adoptionRequests =
                adoptionRequestRepository.findAllByPetID(id);

        for(AdoptionRequest adoptionRequest : adoptionRequests) {

            if (!adoptionRequest.isApproved()) {

                //send message to notification_service
                NotificationAdoptServiceMessage notificationAdoptServiceMessage = new NotificationAdoptServiceMessage(adoptionRequest.getUserID(),
                        adoptionRequest.getId(), false, false);
                rabbitTemplate.convertAndSend(MessagingConfig.ADOPT_NOTIFICATION_SERVICE_EXCHANGE,
                        MessagingConfig.ADOPT_NOTIFICATION_SERVICE_ROUTING_KEY, notificationAdoptServiceMessage);

            }

            //dodavanje u listu u slucaju da dodje do greske pa da se mogu vratiti...
            allRequestsForDelete.add(adoptionRequest);
            adoptionRequestRepository.deleteById(adoptionRequest.getId());
        }
    }

    public void addBackAllDeletedRequests(){
        for(AdoptionRequest adoptionRequest : allRequestsForDelete){
            adoptionRequestRepository.save(adoptionRequest);
        }
    }

}
