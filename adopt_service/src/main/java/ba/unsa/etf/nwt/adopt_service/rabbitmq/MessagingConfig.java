package ba.unsa.etf.nwt.adopt_service.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    public static final String ADOPT_NOTIFICATION_SERVICE_QUEUE = "adopt_notification_service_queue";
    public static final String ADOPT_NOTIFICATION_SERVICE_EXCHANGE = "adopt_notification_service_exchange";
    public static final String ADOPT_NOTIFICATION_SERVICE_ROUTING_KEY = "adopt_notification_service_routing_key";

    public static final String ADOPT_PET_SERVICE_QUEUE = "adopt_pet_service_queue";
    public static final String ADOPT_PET_SERVICE_EXCHANGE = "adopt_pet_service_exchange";
    public static final String ADOPT_PET_SERVICE_ROUTING_KEY = "adopt_pet_service_routing_key";

    public static final String PET_ADOPT_SERVICE_QUEUE = "pet_category_adopt_service_queue";
    public static final String PET_ADOPT_SERVICE_EXCHANGE = "pet_category_adopt_service_exchange";
    public static final String PET_ADOPT_SERVICE_ROUTING_KEY = "pet_category_adopt_service_routing_key";

    @Bean
    public Queue queue1(){
        return new Queue(ADOPT_NOTIFICATION_SERVICE_QUEUE);
    }

    @Bean
    public Queue queue2(){
        return new Queue(ADOPT_PET_SERVICE_QUEUE);
    }

    @Bean
    public Queue queue3(){
        return new Queue(PET_ADOPT_SERVICE_QUEUE);
    }

    @Bean
    public TopicExchange exchange1(){
        return new TopicExchange(ADOPT_NOTIFICATION_SERVICE_EXCHANGE);
    }

    @Bean
    public TopicExchange exchange2(){
        return new TopicExchange(ADOPT_PET_SERVICE_EXCHANGE);
    }

    @Bean
    public TopicExchange exchange3(){
        return new TopicExchange(PET_ADOPT_SERVICE_EXCHANGE);
    }

    @Bean
    public Binding binding1(){
        return BindingBuilder
                .bind(queue1())
                .to(exchange1())
                .with(ADOPT_NOTIFICATION_SERVICE_ROUTING_KEY);
    }

    @Bean
    public Binding binding2(){
        return BindingBuilder
                .bind(queue2())
                .to(exchange2())
                .with(ADOPT_PET_SERVICE_ROUTING_KEY);
    }

    @Bean
    public Binding binding3(){
        return BindingBuilder
                .bind(queue3())
                .to(exchange3())
                .with(PET_ADOPT_SERVICE_ROUTING_KEY);
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
