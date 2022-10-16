package ba.unsa.etf.nwt.pet_category_service.rabbitmq.consumer;

import ba.unsa.etf.nwt.pet_category_service.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConsumer {

    @Autowired
    private PetService petService;

    @RabbitListener(queues = "adopt_pet_service_queue")
    public void consumeMessageFromUserQueue(Long id){
        petService.addPetBack();
    }
}
