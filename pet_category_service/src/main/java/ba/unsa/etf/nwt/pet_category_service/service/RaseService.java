package ba.unsa.etf.nwt.pet_category_service.service;

import ba.unsa.etf.nwt.pet_category_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.pet_category_service.exception.WrongInputException;
import ba.unsa.etf.nwt.pet_category_service.model.Category;
import ba.unsa.etf.nwt.pet_category_service.model.Rase;
import ba.unsa.etf.nwt.pet_category_service.repository.CategoryRepository;
import ba.unsa.etf.nwt.pet_category_service.repository.RaseRepository;
import ba.unsa.etf.nwt.pet_category_service.request.RaseRequest;
import ba.unsa.etf.nwt.pet_category_service.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class RaseService {

    private final RaseRepository raseRepository;
    private final CategoryService categoryService;

    @Autowired
    private CommunicationsService communicationsService;

    public List<Rase> getRases() {
        return raseRepository.findAll();
    }

    public ResponseMessage addRase(RaseRequest raseRequest) {
        try {
            Rase r = findRaseByName(raseRequest.getName());
            throw new WrongInputException("Rase with that name already exists!");
        } catch (ResourceNotFoundException e) {
            Rase rase = new Rase();
            rase.setName(raseRequest.getName());
            rase.setDescription(raseRequest.getDescription());

            Category category = categoryService.getCategoryById(raseRequest.getCategory_id());
            rase.setCategory(category);
            raseRepository.save(rase);

            return new ResponseMessage(true, HttpStatus.OK, "Rase successfully added!!");
        }
    }

    public Rase saveRase(Rase rase){
        return raseRepository.save(rase);
    }

    public Rase getRase(Long id) {
        Rase r = getRaseById(id);
        return r;

    }

    public Rase getRaseById(Long id) {
        return raseRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No rase with ID " + id));
    }

    public ResponseMessage deleteRase(Long id) {
        Rase r = getRaseById(id);

        //moraju se i svi petovi ove rase automatski obrisat
        //petService.deletePet(r.getId());

        communicationsService.deletePetCascade(id);

        raseRepository.deleteById(id);

        return new ResponseMessage(true, HttpStatus.OK, "Rase successfully deleted");
    }

    public Rase findRaseByName(String name) {
        return raseRepository
                .findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("No rase with name " + name));
    }

    public Rase getRaseByName(String name) {
        //if(name == null) return new RaseResponse(null, "Add a name for search!", "BAD_REQUEST", false);
        return findRaseByName(name);
        //return r;
    }

    public List<Rase> getRasesInCategory(Long id) {
        //trebalo bi provjeriti da li kategorija postoji u bazi
        //ali mozda i ne treba jer ce korisnik kliknuti na tu kategoriju znaci da ako je ponudjena onda i postoji u bazi
        return raseRepository.findByCategory_Id(id);
    }

    public Rase updateRase(Long id, RaseRequest raseRequest) {
        Rase r = getRase(id);

        try {
            Rase rr = getRaseByName(raseRequest.getName());
            throw new WrongInputException("Rase with that name already exists!");
        } catch (ResourceNotFoundException e){
            Category cr = categoryService.getCategory(raseRequest.getCategory_id());
            r.setName(raseRequest.getName());
            r.setDescription(raseRequest.getDescription());
            r.setCategory(cr);
            raseRepository.save(r);
            return r;
        }
    }

    public List<Rase> filterByNameContains(String name){

        List<Rase> rases = new ArrayList<>();
        List<Rase> allrases = getRases();

        for(Rase rase : allrases){
            if(rase.getName().toLowerCase().contains(name.toLowerCase())){
                rases.add(rase);
            }
        }

        return rases;
    }

    public List<Rase> filterByNameContainsInThisCategory(Long id, String name){

        List<Rase> rases = new ArrayList<>();
        List<Rase> allrases = getRasesInCategory(id);

        for(Rase rase : allrases){
            if(rase.getName().toLowerCase().contains(name.toLowerCase())){
                rases.add(rase);
            }
        }

        return rases;
    }

    public void deleteRaseCascade(Long categoryId){
        List<Rase> allRases = getRases();

        for(Rase r : allRases){
            if(r.getCategory().getId().equals(categoryId)){
                ResponseMessage responseMessage = deleteRase(r.getId());
            }
        }
    }
}
