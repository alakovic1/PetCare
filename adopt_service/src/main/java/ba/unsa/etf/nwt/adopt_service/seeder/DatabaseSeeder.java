package ba.unsa.etf.nwt.adopt_service.seeder;

import ba.unsa.etf.nwt.adopt_service.model.AddPetRequest;
import ba.unsa.etf.nwt.adopt_service.model.AdoptionRequest;
import ba.unsa.etf.nwt.adopt_service.repository.AddPetRequestRepository;
import ba.unsa.etf.nwt.adopt_service.repository.AdoptionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder {

    @Autowired
    private AddPetRequestRepository addPetRequestRepository;

    @Autowired
    private AdoptionRequestRepository adoptionRequestRepository;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedDatabase();
    }

    private void seedDatabase() {
        createAdoptionRequest(5L, 1L, "Hi! I'd like to adopt your wonderful pet!", false);
        createAdoptionRequest(2L, 2L, "Hi! Your pet looks it needs a new home, I'll be happy to help.", false);

        createAddPetRequest(3L, 3L, "I found this beautiful cat in front of my door. Could you give it a new home?", true);
        createAddPetRequest(5L, 4L, "I can't take care of my fish any more, I got a cat. Maybe you can help me find it a new home?", true);
        createAddPetRequest(5L, 5L, "This is Pricalica. I found her on a tree very scared, probably attacked. She needs a new home. Is there any way to give him a loving family?", true);
    }

    private void createAddPetRequest(Long userID, Long newPetID, String message, boolean approved) {
        AddPetRequest addPetRequest = new AddPetRequest();
        addPetRequest.setUserID(userID);
        addPetRequest.setNewPetID(newPetID);
        addPetRequest.setMessage(message);
        addPetRequest.setApproved(approved);
        addPetRequestRepository.save(addPetRequest);
    }

    private void createAdoptionRequest(Long userID, Long PetID, String message, boolean approved) {
        AdoptionRequest adoptionRequest = new AdoptionRequest();
        adoptionRequest.setUserID(userID);
        adoptionRequest.setPetID(PetID);
        adoptionRequest.setMessage(message);
        adoptionRequest.setApproved(approved);
        adoptionRequestRepository.save(adoptionRequest);
    }
}
