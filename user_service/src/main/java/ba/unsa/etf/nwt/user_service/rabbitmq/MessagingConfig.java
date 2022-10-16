package ba.unsa.etf.nwt.user_service.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    public static final String USER_COMMENT_SERVICE_QUEUE = "user_comment_service_queue";
    public static final String USER_COMMENT_SERVICE_EXCHANGE = "user_comment_service_exchange";
    public static final String USER_COMMENT_SERVICE_ROUTING_KEY = "user_comment_service_routing_key";

    public static final String USER_NOTIFICATION_SERVICE_QUEUE = "user_notification_service_queue";
    public static final String USER_NOTIFICATION_SERVICE_EXCHANGE = "user_notification_service_exchange";
    public static final String USER_NOTIFICATION_SERVICE_ROUTING_KEY = "user_notification_service_routing_key";
    
    @Bean
    public Queue queue1(){
        return new Queue(USER_COMMENT_SERVICE_QUEUE);
    }

    @Bean
    public Queue queue2(){
        return new Queue(USER_NOTIFICATION_SERVICE_QUEUE);
    }

    @Bean
    public TopicExchange exchange1(){
        return new TopicExchange(USER_COMMENT_SERVICE_EXCHANGE);
    }

    @Bean
    public TopicExchange exchange2(){
        return new TopicExchange(USER_NOTIFICATION_SERVICE_EXCHANGE);
    }

    @Bean
    public Binding binding1(){
        return BindingBuilder
                .bind(queue1())
                .to(exchange1())
                .with(USER_COMMENT_SERVICE_ROUTING_KEY);
    }

    @Bean
    public Binding binding2(){
        return BindingBuilder
                .bind(queue2())
                .to(exchange2())
                .with(USER_NOTIFICATION_SERVICE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
