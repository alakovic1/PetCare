package ba.unsa.etf.nwt.pet_category_service.service;

import ba.unsa.etf.nwt.pet_category_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.pet_category_service.model.Pet;
import ba.unsa.etf.nwt.pet_category_service.model.Rase;
import ba.unsa.etf.nwt.pet_category_service.rabbitmq.MessagingConfig;
import ba.unsa.etf.nwt.pet_category_service.repository.PetRepository;
import ba.unsa.etf.nwt.pet_category_service.request.PetRequest;
import ba.unsa.etf.nwt.pet_category_service.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class PetService {
    private final PetRepository petRepository;
    private final RaseService raseService;
    private final CommunicationsService communicationsService;

    private List<Pet> pet;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    public String findPhotoAbsolutePath(MultipartFile multipartFile){
        try {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            String uploadDir = "./uploads/";

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                //return filePath.toFile().getAbsolutePath();
                return "http://localhost:8084/pet-photos/" + fileName;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return "";
        } catch (Exception e){
            System.out.println("The photo couldn't be uploaded!");
        }

        return "";
    }

    private Pet addNewPet(PetRequest petRequest, Boolean isApproved){
        Integer age = petRequest.getAge();
        Pet pet = new Pet();
        pet.setName(petRequest.getName());
        pet.setLocation(petRequest.getLocation());
        pet.setImage(petRequest.getImage());
        pet.setAge(petRequest.getAge());
        pet.setApproved(isApproved);
        pet.setDescription(petRequest.getDescription());

        Rase r = raseService.getRaseById(petRequest.getRase_id());
        pet.setRase(r);
        petRepository.save(pet);

        return pet;
    }

    public ResponseMessage addPet(PetRequest petRequest) {
       // try{
            Pet p = addNewPet(petRequest, true);

            return new ResponseMessage(true, HttpStatus.OK,"Pet successfully added!!");
/*
        }catch (NullPointerException e){
            return new Response(false, "Add the age of the pet!", HttpStatus.BAD_REQUEST);

        }*/

    }

    public Long addPetForAdopt(PetRequest petRequest) {
        // try{
        Pet p = addNewPet(petRequest, false);

        return p.getId();
/*
        }catch (NullPointerException e){
            return new Response(false, "Add the age of the pet!", HttpStatus.BAD_REQUEST);

        }*/

    }


    public void savePet(Pet p) {
        petRepository.save(p);
    }

    public Pet getPetById(Long id) {
        return petRepository
                .findByIdAndApproved(id, true)
                .orElseThrow(() -> new ResourceNotFoundException("No pet with ID " + id));
    }

    public Pet getPet(Long id) {
        return getPetById(id);
        //return p;
    }

    public ResponseMessage deletePet(Long id) {
        //Pet p = getPetById(id);
        petRepository.deleteById(id);
        return new ResponseMessage(true, HttpStatus.OK,"Pet successfully deleted!");
    }

    public List<Pet> findPetByName(String name) {
        List<Pet> petoviSaIstimImenom = petRepository.findPetsByNameAndApproved(name, true);
        if(petoviSaIstimImenom.isEmpty()) throw new ResourceNotFoundException("No pet with name " + name);
        return petoviSaIstimImenom;
    }

    public List<Pet> getPetByName(String name) {
        //if(name == null) return new PetResponse(null, "Add a name for search!", "BAD_REQUEST", false);
        return findPetByName(name);
        //return p;
    }

    public List<Pet> getPetsInRase(Long id) {
        return petRepository.findByRase_IdAndApproved(id, true);
    }

    public Pet updatePet(Long id, PetRequest petRequest) {
        Pet p = getPet(id);

       // try{
            Integer age = petRequest.getAge();

            Rase r= raseService.getRase(petRequest.getRase_id());

            p.setName(petRequest.getName());
            p.setLocation(petRequest.getLocation());
            p.setDescription(petRequest.getDescription());
            p.setImage(petRequest.getImage());
            p.setAge(petRequest.getAge());
            p.setRase(r);
            petRepository.save(p);
            return p;
    /*    }catch (NullPointerException e){
            return new Response(false, "Add the age of the pet!", HttpStatus.OK);

        }*/
    }

    public List<Pet> getPetsInCategory(Long id) {
        //vraca sve petove koji pripadaju nekoj kategoriji i koji su approveani
        //znaci vraca sve cuke, ili sve mace, ili sve ribe i sl
        //find all pets where rase.getCategoryID = id
        return petRepository.findByRase_Category_IdAndApproved(id, true);
    }

    public List<Pet> getPetsNameContainsString(String substring) {

        List<Pet> pets = new ArrayList<>();
        List<Pet> allPets = findAllApprovedPets();

        for(Pet pet : allPets){
            if(pet.getName().toLowerCase().contains(substring.toLowerCase())){
                pets.add(pet);
            }
        }

        return pets;

        //return petRepository.findByNameContainsAndApproved(substring, true);
    }

    public List<Pet> getPetsNameContainsStringinRase(Long id, String substring) {

        List<Pet> pets = new ArrayList<>();
        List<Pet> allPets = getPetsInRase(id);

        for(Pet pet : allPets){
            if(pet.getName().toLowerCase().contains(substring.toLowerCase())){
                pets.add(pet);
            }
        }

        return pets;

        //return petRepository.findByNameContainsAndApproved(substring, true);
    }

    public List<Pet> getPetsRaseContainsString(String substring) {

        List<Pet> pets = new ArrayList<>();
        List<Pet> allPets = findAllApprovedPets();

        for(Pet pet : allPets){
            if(pet.getRase().getName().toLowerCase().contains(substring.toLowerCase())){
                pets.add(pet);
            }
        }

        return pets;

        //return petRepository.findByRase_NameContainsAndApproved(substring, true);
    }

    public Long getPetIDForAdopt() {
        List<Pet> sviPetovi = getPets();
        Pet p = sviPetovi.get(sviPetovi.size() - 1);
        return p.getId();
    }

    public List<Pet> findAllApprovedPets(){
        return petRepository.findByApproved(true);
    }

    public ResponseMessage setApproved(Long requestType, Long petID){

        Pet pet = petRepository.findById(petID)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found!"));

        //ako je adopt u pitanju, onda se taj pet approvea i ne prikazuje se korisniku
        //u bazi se oznacava kao da nije approvan, ali jeste, odnosno samo je adoptan
        if(requestType == 1L){
            //petRepository.deleteById(petID);
            pet.setApproved(false);
        } else {
            pet.setApproved(true);
        }

        petRepository.save(pet);

        return new ResponseMessage(true, HttpStatus.OK,
                "You have successfully approved this pet!");
    }

    public ResponseMessage deletePetById(Long id){

        //send message to adopt_service
        rabbitTemplate.convertAndSend(MessagingConfig.PET_ADOPT_SERVICE_EXCHANGE,
                MessagingConfig.PET_ADOPT_SERVICE_ROUTING_KEY, id);

        pet = new ArrayList<>();

        Pet p = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found!"));

        pet.add(p);

        petRepository.deleteById(id);

        return new ResponseMessage(true, HttpStatus.OK, "Pet deleted!");
    }

    public void addPetBack(){
        for(Pet p : pet){
            petRepository.save(p);
        }
    }

    public Pet getPetForAdmin(Long id){
        return petRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No pet with ID " + id));
    }

    public void deletePetCascade(Long raseId){
        List<Pet> allPets = getPets();

        for(Pet p : allPets){
            if(p.getRase().getId().equals(raseId)){
                ResponseMessage responseMessage = deletePetById(p.getId());
            }
        }
    }
}
