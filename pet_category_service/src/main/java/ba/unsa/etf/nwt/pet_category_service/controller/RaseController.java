package ba.unsa.etf.nwt.pet_category_service.controller;

import ba.unsa.etf.nwt.pet_category_service.model.Category;
import ba.unsa.etf.nwt.pet_category_service.model.Rase;
import ba.unsa.etf.nwt.pet_category_service.request.RaseRequest;
import ba.unsa.etf.nwt.pet_category_service.response.ResponseMessage;
import ba.unsa.etf.nwt.pet_category_service.service.RaseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
public class RaseController {

    private final RaseService raseService;

    //tehnicki nam ne treba
    @GetMapping("/rases")
    public List<Rase> getRases(){
        return raseService.getRases();
    }

    @GetMapping("/rases/inCategory")
    public List<Rase> getRasesInCategory(@NotNull @RequestParam Long id){
        return raseService.getRasesInCategory(id);
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/rase/{id}")
    public Rase getRase(@NotNull @PathVariable Long id){
        return raseService.getRase(id);
    }

    @GetMapping("/rase/byName")
    public Rase getRaseByName(@NotNull @RequestParam String name){
        return raseService.getRaseByName(name);
    }

    //search
    @GetMapping("/rases/filterByName/contains")
    public List<Rase> getAllRasesThatContainName(@NotNull @RequestParam String name){
        return raseService.filterByNameContains(name);
    }

    //search rasa u toj kategoriji
    @GetMapping("/rases/filterByName/contains/thisCategory/{id}")
    public List<Rase> getAllRasesFromThisCategoryThatContainName(@PathVariable Long id ,@NotNull @RequestParam String name){
        return raseService.filterByNameContainsInThisCategory(id, name);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/rase")
    public ResponseMessage addRase(@Valid @RequestBody RaseRequest raseRequest){
       return raseService.addRase(raseRequest);
    }

    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("/rase")
    public ResponseMessage deleteRase(@NotNull @RequestParam Long id){
        return raseService.deleteRase(id);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PutMapping("/rase/update/{id}")
    public Rase updateRase(@NotNull @PathVariable Long id, @Valid @RequestBody RaseRequest raseRequest){
        return raseService.updateRase(id, raseRequest);
    }

}
