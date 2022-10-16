package ba.unsa.etf.nwt.user_service.controller;

import ba.unsa.etf.nwt.user_service.email.EmailCfg;
import ba.unsa.etf.nwt.user_service.email.ContactUsForm;
import ba.unsa.etf.nwt.user_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.user_service.rabbitmq.MessagingConfig;
import ba.unsa.etf.nwt.user_service.rabbitmq.NotificationServiceMessage;
import ba.unsa.etf.nwt.user_service.response.ResponseMessage;
import ba.unsa.etf.nwt.user_service.service.CommunicationsService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailCfg emailCfg;

    private final CommunicationsService communicationsService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public EmailController(EmailCfg emailCfg, CommunicationsService communicationsService) {
        this.emailCfg = emailCfg;
        this.communicationsService = communicationsService;
    }

    @PostMapping("/send")
    public ResponseMessage sendEmail(@Valid @RequestBody ContactUsForm contactUsForm){

        try {
            //Create mail sender
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(this.emailCfg.getHost());
            mailSender.setPort(this.emailCfg.getPort());
            mailSender.setUsername(this.emailCfg.getUsername());
            mailSender.setPassword(this.emailCfg.getPassword());

            //Set properties for gmail server
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            //Create an email instance
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(contactUsForm.getEmail());
            mailMessage.setTo("nwt.pet.care.adm2021@gmail.com");
            mailMessage.setSubject("New email from contact us form!");

            String date = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss").format(new Date());

            String message = "Time when the form was filled: \n" + date + "\n\n" +
                    "Contact us form was filled by " + contactUsForm.getName() + ". \n" +
                    "To contact this person, send a response to " + contactUsForm.getEmail() + " address." + "\n\n" +
                    "Message: \n";

            mailMessage.setText(message + contactUsForm.getMessage());

            //Send mail
            mailSender.send(mailMessage);

            /*RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseMessage responseMessage = restTemplate.getForObject(communicationsService.getUri("notification_service")
                        + "/notifications/public/add/-1", ResponseMessage.class);
                System.out.println(responseMessage.getMessage());
            } catch (Exception ue){
                System.out.println("Can't connect to notification_service!");
            }*/

            //send message to notification_service
            NotificationServiceMessage notificationServiceMessage = new NotificationServiceMessage(-1L,
                    "Someone filled contact us form, check email!");
            rabbitTemplate.convertAndSend(MessagingConfig.USER_NOTIFICATION_SERVICE_EXCHANGE,
                    MessagingConfig.USER_NOTIFICATION_SERVICE_ROUTING_KEY, notificationServiceMessage);


            return new ResponseMessage(true, HttpStatus.OK, "You have successfully sent an email!");
        }
        catch (Exception e){
            throw new ResourceNotFoundException("Can't connect to email server!");
        }
    }

}
