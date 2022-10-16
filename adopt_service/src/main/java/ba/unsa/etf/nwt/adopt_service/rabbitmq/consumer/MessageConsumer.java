package ba.unsa.etf.nwt.adopt_service.rabbitmq.consumer;

import ba.unsa.etf.nwt.adopt_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.adopt_service.rabbitmq.MessagingConfig;
import ba.unsa.etf.nwt.adopt_service.rabbitmq.NotificationAdoptServiceMessage;
import ba.unsa.etf.nwt.adopt_service.service.AddPetRequestService;
import ba.unsa.etf.nwt.adopt_service.service.AdoptionRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConsumer {

    @Autowired
    private AdoptionRequestService adoptionRequestService;

    @Autowired
    private AddPetRequestService addPetRequestService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "pet_category_adopt_service_queue")
    public void consumeMessageFromUserQueue(Long id){
        try {
            adoptionRequestService.findAndDeleteAdoptionRequest(id);
            addPetRequestService.findAndDeleteAddPetRequest(id);

            //throw new ResourceNotFoundException("New exception was thrown!");
        } catch (Exception e){

            //ako SLUCAJNO dodje do greske salje se
            //notifikacija adminu da pet nije obrisan i vraca se info petu da se vrati pet u bazu

            //send message to notification_service
            NotificationAdoptServiceMessage notificationAdoptServiceMessage = new NotificationAdoptServiceMessage(-2L,
                    id, false, true);
            rabbitTemplate.convertAndSend(MessagingConfig.ADOPT_NOTIFICATION_SERVICE_EXCHANGE,
                    MessagingConfig.ADOPT_NOTIFICATION_SERVICE_ROUTING_KEY, notificationAdoptServiceMessage);

            //vracaju se svi obrisani reguestovi
            addPetRequestService.addBackAllDeletedRequests();
            adoptionRequestService.addBackAllDeletedRequests();

            //send message to pet_category_service
            rabbitTemplate.convertAndSend(MessagingConfig.ADOPT_PET_SERVICE_EXCHANGE,
                    MessagingConfig.ADOPT_PET_SERVICE_ROUTING_KEY, id);

        }
    }
}
