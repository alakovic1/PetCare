package ba.unsa.etf.nwt.comment_service.rabbitmq;

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

    @Bean
    public Queue queue(){
        return new Queue(USER_COMMENT_SERVICE_QUEUE);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(USER_COMMENT_SERVICE_EXCHANGE);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(USER_COMMENT_SERVICE_ROUTING_KEY);
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
