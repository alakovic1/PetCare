package ba.unsa.etf.nwt.comment_service.rabbitmq;

import ba.unsa.etf.nwt.comment_service.service.CommentService;
import ba.unsa.etf.nwt.comment_service.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConsumer {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReplyService replyService;

    @RabbitListener(queues = "user_comment_service_queue")
    public void consumeMessageFromQueue(CommentServiceMessage commentServiceMessage){
        System.out.println("Message from RabbitMQ: " + commentServiceMessage.getMessage());
        commentService.setUsernameOnUnknown(commentServiceMessage.getUsername());
        replyService.setUsernameOnUnknown(commentServiceMessage.getUsername());
    }
}
