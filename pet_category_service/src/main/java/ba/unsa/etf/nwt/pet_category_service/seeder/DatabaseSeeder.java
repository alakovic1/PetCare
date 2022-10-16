package ba.unsa.etf.nwt.pet_category_service.seeder;

import ba.unsa.etf.nwt.pet_category_service.model.Category;
import ba.unsa.etf.nwt.pet_category_service.model.Pet;
import ba.unsa.etf.nwt.pet_category_service.model.Rase;
import ba.unsa.etf.nwt.pet_category_service.service.CategoryService;
import ba.unsa.etf.nwt.pet_category_service.service.PetService;
import ba.unsa.etf.nwt.pet_category_service.service.RaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DatabaseSeeder {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PetService petService;

    @Autowired
    private RaseService raseService;

    @EventListener
    public void seed(ContextRefreshedEvent event){
        seed();
    }

    private void seed() {

        //kategorije

        Category c1 = createCategory("Dog", "Dogs are domesticated mammals, not natural wild animals. ");
        Category c2 = createCategory("Cat", "Cats, also called domestic cats (Felis catus), are small, carnivorous (meat-eating) mammals, of the family Felidae.");
        Category c3 = createCategory("Fish", "Fish (plural: fish) are an aquatic group of vertebrates which live in water and respire (get oxygen) with gills. They do not have limbs.");
        Category c4 = createCategory("Bird", "Birds can be wonderful companions for people who are single or live alone. Their chatter and antics can be quite amusing.");
        Category c5 = createCategory("Other", "");

        //rase

        Rase r1 = createRase("American bulldog", "The American Bulldog is stocky and muscular, but also agile and built for chasing down stray cattle and helping with farm work.American Bulldogs are intelligent.", c1);
        Rase r2 = createRase("Bichon Frise", "The Bichon Frise is a cheerful, small dog breed with a love of mischief and a lot of love to give.",c1);
        Rase r3 = createRase("British Shorthair", "The British Shorthair is an easygoing feline. She enjoys affection but isn’t needy and dislikes being carried.", c2);
        Rase r4 = createRase("Goldfish", "Another cold-water fish, goldfish belong to the carp family. Because they enjoy cool water temperatures, keep goldfish in a separate tank from warm water fish.", c3);
        Rase r5 = createRase("Budgerigar", "Enjoying popularity around the world, budgies (also known as parakeets) are some of the best pet birds for good reason.", c4);
        Rase r6 = createRase("Belgian Shepherd", "Belgian Shepherds are known to be highly intelligent, alert and sensitive; they are typically highly trainable, vigilant and hard-working with a strong guarding instinct making them protective of property and family, and very well suited for service with security services.", c1);
        Rase r8 = createRase("Chihuahua", "The Chihuahua dog breed‘s charms include their small size, big personality, and variety in coat types and colors. They’re all dog, fully capable of competing in dog sports such as agility and obedience, and are among the top ten watchdogs recommended by experts.", c1);
        Rase r9 = createRase("Persian cat", "Persian Cats are known for their gentle, quiet, docile nature. They are very sweet cats, but can be discriminating in their affection, only giving their attention to people they trust.", c2);
        Rase r12 = createRase("Clownfish", "Bright orange with three distinctive white bars, clown anemonefish are among the most recognizable of all reef-dwellers. They reach about 4.3 inches in length, and are named for the multicolored sea anemone in which they make their homes.", c3);
        Rase r14 = createRase("Red factor canary", "The red factor canary is a popular variety of canary. It is named after its colourful plumage, and is a 'color canary', bred for the novelty of its color rather than for its song.", c4);
        Rase r15 = createRase("Corn Snakes", "They are the ultimate starter snake for anyone joining the hobby as they rarely miss a meal and are really easy to look after. Corn snakes can grow to 6ft (usually not though) but always remain thin bodied making them a good choice for people who aren’t as fond of the heavy bodied snakes.", c5);
        Rase r16 = createRase("Other", "", c5);
        Rase r17 = createRase("Other", "", c1);
        Rase r18 = createRase("Other", "", c2);
        Rase r19 = createRase("Other", "", c3);
        Rase r20 = createRase("Other", "", c4);

        //ljubimci

        createPet("Rex", "Sarajevo", "http://localhost:8084/pet-photos/american_bulldog.jpg", "This American Bulldog may look tough, but in reality he is very spoiled, loves children and is very friendly with everyone. He is used to a big group of people and gets along with other animals very well.", 2, true, r1);
        createPet("Pupi", "Tuzla", "http://localhost:8084/pet-photos/bichon_frise.jpg", "This Bichon Frise is a big diva. He likes his haircuts and makeovers. He is always the center of attention and a great friend to a man.", 1, true, r2);
        createPet("Cicko", "Zenica", "http://localhost:8084/pet-photos/british_shorthair.jpg", "This British Shothair is a bit older cat, but it will give you all the love it has. She is super lazy, and loves sleeping on the couch, especially if there is a human next to her.", 9, true, r3);
        createPet("Ribica", "Neum", "http://localhost:8084/pet-photos/goldfish.jpg", "A goldfish is always a good addition to your aquarium. Maybe this one will grant you the famous 3 wishes... :D", 0, true, r4);
        createPet("Pricalica", "Brcko", "http://localhost:8084/pet-photos/budgerigar.jpg", "This Budgerigar loves hanging out in his cage, and in the high corner of a room. Sometimes, he will even say something, repeat something.", 1, true, r5);
        createPet("Hades", "Čapljina", "http://localhost:8084/pet-photos/adult-corn-snake.jpg", "Snakes are maybe scary. This one is also named after the God of the underworld, but it's the friendliest snake you will meet.", 1, true, r15);
        createPet("Bella", "Sarajevo", "http://localhost:8084/pet-photos/bella.jpg", "Very friendly and trained dog. Used to people and kids.", 5, true, r6);
        createPet("Stella", "Visoko", "http://localhost:8084/pet-photos/stella.jpg", "Stella is an old police dog, very good trained and listens to everything.", 10, true, r6);
        createPet("Brundo", "Jajce", "http://localhost:8084/pet-photos/brundo.jpg", "This Belgian Shepherd is a puppy that needs some love and attention. He is used to other animals, and loves playing with his toys.", 0, true, r6);
        createPet("Princess", "Mostar", "http://localhost:8084/pet-photos/princess.jpg", "This spoiled diva will be you perfect accessory, but she is not just for a handbag. She loves to play with other dog, especially big dogs.", 1, true, r8);
        createPet("Coco", "Banja Luka", "http://localhost:8084/pet-photos/coco.jpg", "Has a big personality and will protect you in any danger.", 4, true, r8);
        createPet("Diva", "Zenica", "http://localhost:8084/pet-photos/diva.jpg","This fluffy angel will make your life more beautiful.", 0, true, r9);
        createPet("Akira", "Mostar", "http://localhost:8084/pet-photos/akira.jpg","This is the cat for you. She knows how to use her litter box and doesnt leave a mess behind her. Als, she is very clumsy so your life wont be boring.", 1, true, r9);
        createPet("Garfield", "Neum", "http://localhost:8084/pet-photos/garfield.jpg", "If you want your own Nemo, this is it... Adopt me..", 0, true, r12);
        createPet("Crvenko", "Sarajevo", "http://localhost:8084/pet-photos/crvenko.jpg", "This pet loves to hand out in his cage and loves singing to any song on you put on.", 0, true, r14);

    }

    private Category createCategory(String name, String description){
        Category c = new Category();
        c.setName(name);
        c.setDescription(description);
        categoryService.saveCategory(c);
        return c;
    }

    private Rase createRase(String name, String description, Category category){
        Rase r = new Rase();
        r.setName(name);
        r.setDescription(description);
        r.setCategory(category);
        raseService.saveRase(r);
        return r;
    }

    private void createPet(String name, String location, String image, String description, int age, boolean approved, Rase rase){
        Pet p = new Pet();
        p.setName(name);
        p.setLocation(location);
        p.setImage(image);
        p.setDescription(description);
        p.setAge(age);
        p.setApproved(approved);
        p.setRase(rase);
        petService.savePet(p);
        //p.setId(1L);
    }
}
