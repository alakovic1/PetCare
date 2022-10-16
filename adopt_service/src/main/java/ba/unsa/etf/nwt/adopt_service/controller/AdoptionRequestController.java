package ba.unsa.etf.nwt.adopt_service.controller;

import ba.unsa.etf.nwt.adopt_service.exception.WrongInputException;
import ba.unsa.etf.nwt.adopt_service.model.AdoptionRequest;
import ba.unsa.etf.nwt.adopt_service.request.PetForAdoptRequest;
import ba.unsa.etf.nwt.adopt_service.response.ResponseMessage;
import ba.unsa.etf.nwt.adopt_service.security.CurrentUser;
import ba.unsa.etf.nwt.adopt_service.security.UserPrincipal;
import ba.unsa.etf.nwt.adopt_service.service.AdoptionRequestService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
public class AdoptionRequestController {
    private final AdoptionRequestService adoptionRequestService;

    //ruta vjerovatno nece biti koristena
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/adoption-request")
    public List<AdoptionRequest> getAdoptionRequests() {
        return adoptionRequestService.getAdoptionRequest();
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/eurekaa/adoption-request/{petID}")
    public ResponseMessage addAdoptionRequest(@RequestHeader("Authorization") String token,
                                              @PathVariable Long petID ,
                                              @Valid @RequestBody PetForAdoptRequest petForAdoptRequest,
                                              @CurrentUser UserPrincipal currentUser) {
        return adoptionRequestService.addAdoptionRequest(token, petID, petForAdoptRequest, currentUser);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/adoption-request")
    public ResponseMessage addAdoptionRequestLocal(@Valid @RequestBody AdoptionRequest adoptionRequest) {
        return adoptionRequestService.addAdoptionRequestLocal(adoptionRequest);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/adoption-request/user/{userID}")
    public List<AdoptionRequest> getAdoptionRequests(@PathVariable Long userID, @CurrentUser UserPrincipal currentUser) {

        //pronalazak role trenutnog korisnika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        //svim adminima se vracaju svi requestovi
        if(hasAdminRole){
            return adoptionRequestService.getAdoptionRequest();
        }

        //korisnici mogu obrisati samo vlastite requeste
        if (!currentUser.getId().equals(userID)) {
            throw new WrongInputException("Current user is not admin and this request doesn't belong to current user!");
        }

        return adoptionRequestService.getAdoptionRequestByUserID(userID);
    }

    //filteri
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/adoption-request/pet/{petID}")
    public List<AdoptionRequest> getAdoptionRequestByPetID(@PathVariable Long petID) {
        return adoptionRequestService.getAdoptionRequestByPetID(petID);
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/adoption-request/approved")
    public List<AdoptionRequest> getApprovedAdoptionRequests() {
        return adoptionRequestService.getApprovedAdoptionRequests();
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/adoption-request/not-approved")
    public List<AdoptionRequest> getNotApprovedAdoptionRequests() {
        return adoptionRequestService.getNotApprovedAdoptionRequests();
    }

    //brisanje requesta
    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("/adoption-request/{id}")
    public ResponseMessage deleteAdoptionRequestByID(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        return adoptionRequestService.deleteAdoptionRequestByID(token, id);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PutMapping("/adopt-request/not-approved/{id}")
    public ResponseMessage setAddPetRequestNotApproved(@RequestHeader("Authorization") String token, @PathVariable Long id){
        return adoptionRequestService.setNotApproved(token, id);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PutMapping("/adoption-request/approve/{id}")
    public ResponseMessage approveAdoptionRequest(@RequestHeader("Authorization") String token, @PathVariable Long id){
        return adoptionRequestService.setApproved(token, id);
    }

    //vrati request po id-u, bitno da bi se moglo iz notifikacije doci do requesta da se kasnije approvea
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/adoption-request/{id}")
    public AdoptionRequest getAdoptionRequest(@PathVariable Long id){
        return adoptionRequestService.getAdoptionRequestbyId(id);
    }
}
