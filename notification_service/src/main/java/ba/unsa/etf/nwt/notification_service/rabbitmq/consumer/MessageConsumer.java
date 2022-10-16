package ba.unsa.etf.nwt.notification_service.rabbitmq.consumer;

import ba.unsa.etf.nwt.notification_service.response.ResponseMessage;
import ba.unsa.etf.nwt.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConsumer {

    @Autowired
    private NotificationService notificationService;

    //@Autowired
    //private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "user_notification_service_queue")
    public void consumeMessageFromUserQueue(NotificationServiceMessage notificationServiceMessage){
        /*try {
            notificationService.contactUsAndRegistrationNotification(notificationServiceMessage.getUserId(), notificationServiceMessage.getContent());
        } catch (Exception e){
            //send back message to user_service
            rabbitTemplate.convertAndSend(MessagingConfig.NOTIFICATION_SERVICE_EXCHANGE,
                    MessagingConfig.NOTIFICATION_SERVICE_ROUTING_KEY, notificationServiceMessage);
        }*/

        ResponseMessage responseMessage = notificationService.addNotificationForRegistrationAndContactUs(notificationServiceMessage.getUserId(),
                notificationServiceMessage.getContent());
    }

    @RabbitListener(queues = "adopt_notification_service_queue")
    public void consumeMessageFromAdoptQueue(NotificationAdoptServiceMessage notificationAdoptServiceMessage){
        if(notificationAdoptServiceMessage.getUserId() != -2L) {
            if (notificationAdoptServiceMessage.getIsAdd()) {
                if (notificationAdoptServiceMessage.getApproved()) {
                    ResponseMessage responseMessage = notificationService.addNotificationForNewAddRequestApproved(notificationAdoptServiceMessage.getUserId(),
                            notificationAdoptServiceMessage.getRequestId());
                } else {
                    ResponseMessage responseMessage = notificationService.addNotificationForNewAddRequestNotApproved(notificationAdoptServiceMessage.getUserId(),
                            notificationAdoptServiceMessage.getRequestId());
                }
            } else {
                if (notificationAdoptServiceMessage.getApproved()) {
                    ResponseMessage responseMessage = notificationService.addNotificationForNewAdoptRequestApproved(notificationAdoptServiceMessage.getUserId(),
                            notificationAdoptServiceMessage.getRequestId());
                } else {
                    ResponseMessage responseMessage = notificationService.addNotificationForNewAdoptRequestNotApproved(notificationAdoptServiceMessage.getUserId(),
                            notificationAdoptServiceMessage.getRequestId());
                }
            }
        } else {
            ResponseMessage responseMessage = notificationService.addNotificationForRegistrationAndContactUs(-1L,
                    "Pet with " + notificationAdoptServiceMessage.getRequestId() + " wasn't deleted. It was added back to " +
                            "database as well as all its related requests!");
        }
    }
}
