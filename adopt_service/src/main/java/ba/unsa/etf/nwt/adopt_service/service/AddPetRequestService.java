package ba.unsa.etf.nwt.adopt_service.service;

import ba.unsa.etf.nwt.adopt_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.adopt_service.model.AddPetRequest;
import ba.unsa.etf.nwt.adopt_service.rabbitmq.MessagingConfig;
import ba.unsa.etf.nwt.adopt_service.rabbitmq.NotificationAdoptServiceMessage;
import ba.unsa.etf.nwt.adopt_service.repository.AddPetRequestRepository;
import ba.unsa.etf.nwt.adopt_service.request.PetForAddRequest;
import ba.unsa.etf.nwt.adopt_service.request.PetRequest;
import ba.unsa.etf.nwt.adopt_service.response.ResponseMessage;
import ba.unsa.etf.nwt.adopt_service.security.CurrentUser;
import ba.unsa.etf.nwt.adopt_service.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AddPetRequestService {
    private final AddPetRequestRepository addPetRequestRepository;
    private final CommunicationsService communicationsService;

    private List<AddPetRequest> allRequestsForDelete;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public List<AddPetRequest> getAddPetRequest() {
        return addPetRequestRepository.findAll();
    }

    public String findPhotoAbsolutePath(MultipartFile multipartFile){
        try {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            String uploadDir = "./uploads/";

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                //return filePath.toFile().getAbsolutePath();
                return "http://localhost:8082/pet-photos/" + fileName;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return "";
        } catch (Exception e){
            System.out.println("The photo couldn't be uploaded!");
        }

        return "";
    }

    public ResponseMessage addAddPetRequest(String token, PetForAddRequest addPetRequest,
                                            @CurrentUser UserPrincipal currentUser) {

            RestTemplate restTemplate = new RestTemplate();

            AddPetRequest newRequest = new AddPetRequest();

            newRequest.setUserID(currentUser.getId());

            try {
                //sada prvo moramo dodati poslani pet u bazu preko rute POST u pet servisu

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", token);

                HttpEntity<PetRequest> entityReq = new HttpEntity<PetRequest>(addPetRequest.getPetForAdd(), headers);

                ResponseEntity<Long> petID = restTemplate.exchange(communicationsService.getUri("pet_category_service")
                                + "/petID/forAdopt", HttpMethod.POST, entityReq, Long.class);

                newRequest.setNewPetID(petID.getBody());
                newRequest.setMessage(addPetRequest.getMessage());
                addPetRequestRepository.save(newRequest);

                //dodavanje notifikacije adminu da je dodan request za novi pet

                try {

                    //bitna notifikacije, ide sinhrono...
                    HttpHeaders headers2 = new HttpHeaders();
                    headers2.set("Authorization", token);

                    HttpEntity<String> entityReq2 = new HttpEntity<>("", headers2);

                    ResponseEntity<ResponseMessage> responseMessage2 = restTemplate.exchange(communicationsService.getUri("notification_service")
                                    + "/notifications/add/add-pet-request/" + newRequest.getUserID() + "/" + newRequest.getId(),
                            HttpMethod.GET ,entityReq2, ResponseMessage.class);

                    System.out.println(responseMessage2.getBody().getMessage());
                } catch (Exception ue){
                    throw new ResourceNotFoundException("Can't connect to notification_service!");
                    //System.out.println("Can't connect to notification_service!");
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
                //ako se ne moze spojiti na pet service
                if(e.getMessage().equals("URI is not absolute")) {//ova message se vrati ako nije podignut neki servis
                    throw new ResourceNotFoundException("Can't connect to pet_category_service!!");
                }
                //kad se tamo hendla onaj exception vratit ce nesto
                //i onda bih trebala provjeriti jel wrong input ili resource not found
                if(e.getMessage().contains("rase")){//ako zadana rasa ne postoji u bazi
                    throw new ResourceNotFoundException("No rase with id " + addPetRequest.getPetForAdd().getRase_id());
                }
                //ako nije dodalo novog peta uopce
                throw new ResourceNotFoundException("Pet is not added!");

            }

        return new ResponseMessage(true, HttpStatus.OK, "Request to add a new pet added successfully!");
    }

    public ResponseMessage addAddPetRequestLocal(AddPetRequest addPetRequest) {
        AddPetRequest novi = new AddPetRequest();
        novi.setNewPetID(addPetRequest.getNewPetID());
        novi.setUserID(addPetRequest.getUserID());
        novi.setMessage(addPetRequest.getMessage());
        addPetRequestRepository.save(novi);
        return new ResponseMessage(true, HttpStatus.OK, "Request to add a new pet added successfully!");
    }

    public List<AddPetRequest> getAddPetRequestByUserID(Long userID) {
        return addPetRequestRepository
                .findAll()
                .stream()
                .filter(n -> n.getUserID().equals(userID))
                .collect(Collectors.toList());
    }

    public List<AddPetRequest> getAddPetRequestByNewPetID(Long newPetID) {
        return addPetRequestRepository
                .findAll()
                .stream()
                .filter(n -> n.getNewPetID().equals(newPetID))
                .collect(Collectors.toList());
    }

    public List<AddPetRequest> getApprovedAddPetRequests() {
        return addPetRequestRepository
                .findAll()
                .stream()
                .filter(n -> n.isApproved())
                .collect(Collectors.toList());
    }

    public List<AddPetRequest> getNotApprovedAddPetRequests() {
        return addPetRequestRepository
                .findAll()
                .stream()
                .filter(n -> !n.isApproved())
                .collect(Collectors.toList());
    }

    public ResponseMessage deleteAddPetRequest(String token, Long id){

        AddPetRequest addPetRequest = addPetRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found!"));

        RestTemplate restTemplate = new RestTemplate();

        if(!addPetRequest.isApproved()) {
            try {

                //komunikacija za pet_category_service ide sinhrono
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", token);

                HttpEntity<String> entityReq = new HttpEntity<>("", headers);

                ResponseEntity<ResponseMessage> responseMessage = restTemplate.exchange(communicationsService.getUri("pet_category_service")
                                + "/pet?id=" + addPetRequest.getNewPetID(),
                        HttpMethod.DELETE, entityReq, ResponseMessage.class);

                System.out.println(responseMessage.getBody().getMessage());
            } catch (Exception ue) {
                throw new ResourceNotFoundException("Can't connect to pet_category_service and delete a pet!");
            }

            //send message to notification_service
            NotificationAdoptServiceMessage notificationAdoptServiceMessage = new NotificationAdoptServiceMessage(addPetRequest.getUserID(),
                    addPetRequest.getId(), false, true);
            rabbitTemplate.convertAndSend(MessagingConfig.ADOPT_NOTIFICATION_SERVICE_EXCHANGE,
                    MessagingConfig.ADOPT_NOTIFICATION_SERVICE_ROUTING_KEY, notificationAdoptServiceMessage);

            communicationsService.deleteForAdopt(token, addPetRequest.getNewPetID());

        }

        addPetRequestRepository.deleteById(id);

        return new ResponseMessage(true, HttpStatus.OK, "You have successfully deleted this request!");
    }

    public ResponseMessage setNotApproved(String token, Long id){
        AddPetRequest addPetRequest = addPetRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found!"));

        //send message to notification_service
        NotificationAdoptServiceMessage notificationAdoptServiceMessage = new NotificationAdoptServiceMessage(addPetRequest.getUserID(),
                addPetRequest.getId(), false, true);
        rabbitTemplate.convertAndSend(MessagingConfig.ADOPT_NOTIFICATION_SERVICE_EXCHANGE,
                MessagingConfig.ADOPT_NOTIFICATION_SERVICE_ROUTING_KEY, notificationAdoptServiceMessage);

        addPetRequest.setApproved(false);
        addPetRequestRepository.save(addPetRequest);

        return new ResponseMessage(true, HttpStatus.OK, "You didn't approve this request!");
    }

    public ResponseMessage setApproved(String token, Long id){
        AddPetRequest addPetRequest = addPetRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found!"));

        //poziva se ruta iz pet servisa da se i tamo odobri..

        RestTemplate restTemplate = new RestTemplate();
        try {

            //sinhrono...
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<String> entityReq = new HttpEntity<>("", headers);

            ResponseEntity<ResponseMessage> responseMessage = restTemplate.exchange(communicationsService.getUri("pet_category_service")
                            + "/pets/approve/0/" + addPetRequest.getNewPetID(),
                    HttpMethod.PUT ,entityReq, ResponseMessage.class);

            System.out.println(responseMessage.getBody().getMessage());
        } catch (Exception ue){
            throw new ResourceNotFoundException("Can't connect to pet_category_service and approve a pet!");
        }

        //send message to notification_service
        NotificationAdoptServiceMessage notificationAdoptServiceMessage = new NotificationAdoptServiceMessage(addPetRequest.getUserID(),
                addPetRequest.getId(), true, true);
        rabbitTemplate.convertAndSend(MessagingConfig.ADOPT_NOTIFICATION_SERVICE_EXCHANGE,
                MessagingConfig.ADOPT_NOTIFICATION_SERVICE_ROUTING_KEY, notificationAdoptServiceMessage);

        addPetRequest.setApproved(true);
        addPetRequestRepository.save(addPetRequest);

        return new ResponseMessage(true, HttpStatus.OK, "This request has been approved!");
    }

    public AddPetRequest getAddPetRequestById(Long id){
        return addPetRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found!"));
    }

    public void findAndDeleteAddPetRequest(Long id){
        List<AddPetRequest> addPetRequests =
                addPetRequestRepository.findAllByNewPetID(id);

        for(AddPetRequest addPetRequest : addPetRequests) {

            if (!addPetRequest.isApproved()) {

                //send message to notification_service
                NotificationAdoptServiceMessage notificationAdoptServiceMessage = new NotificationAdoptServiceMessage(addPetRequest.getUserID(),
                        addPetRequest.getId(), false, true);
                rabbitTemplate.convertAndSend(MessagingConfig.ADOPT_NOTIFICATION_SERVICE_EXCHANGE,
                        MessagingConfig.ADOPT_NOTIFICATION_SERVICE_ROUTING_KEY, notificationAdoptServiceMessage);

            }
            //dodavanje u listu u slucaju da dodje do greske pa da se mogu vratiti...
            allRequestsForDelete.add(addPetRequest);
            addPetRequestRepository.deleteById(addPetRequest.getId());
        }
    }

    public void addBackAllDeletedRequests(){
        for(AddPetRequest addPetRequest : allRequestsForDelete){
            addPetRequestRepository.save(addPetRequest);
        }
    }

}
