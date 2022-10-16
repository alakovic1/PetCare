package ba.unsa.etf.nwt.adopt_service;

import ba.unsa.etf.nwt.adopt_service.model.AddPetRequest;
import ba.unsa.etf.nwt.adopt_service.model.AdoptionRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
class AdoptServiceApplicationTests {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void allNullFieldsAdoptionRequest() {
        AdoptionRequest adoptionRequest = new AdoptionRequest();
        Set<ConstraintViolation<AdoptionRequest>> violations = validator.validate(adoptionRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void allNullFieldsAddPetRequest() {
        AddPetRequest addPetRequest = new AddPetRequest();
        Set<ConstraintViolation<AddPetRequest>> violations = validator.validate(addPetRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullUserIDAdoptionRequest() {
        AdoptionRequest adoptionRequest = new AdoptionRequest();
        adoptionRequest.setPetID(1L);
        adoptionRequest.setUserID(null);
        Set<ConstraintViolation<AdoptionRequest>> violations = validator.validate(adoptionRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullUserIDAddPetRequest() {
        AddPetRequest addPetRequest = new AddPetRequest();
        addPetRequest.setNewPetID(1L);
        addPetRequest.setUserID(null);
        Set<ConstraintViolation<AddPetRequest>> violations = validator.validate(addPetRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullPetIDAdoptionRequest() {
        AdoptionRequest adoptionRequest = new AdoptionRequest();
        adoptionRequest.setPetID(null);
        adoptionRequest.setUserID(1L);
        Set<ConstraintViolation<AdoptionRequest>> violations = validator.validate(adoptionRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullNewPetIDAdddPetRequest() {
        AddPetRequest addPetRequest = new AddPetRequest();
        addPetRequest.setNewPetID(null);
        addPetRequest.setUserID(1L);
        Set<ConstraintViolation<AddPetRequest>> violations = validator.validate(addPetRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void messageTooLongAdoptionRequest() {
        AdoptionRequest adoptionRequest = new AdoptionRequest();
        adoptionRequest.setUserID(1L);
        adoptionRequest.setPetID(1L);
        adoptionRequest.setMessage("Što se tiče lokalnih stranica, ova stranica je kao DogsTrust (http://www.dogstrust.ba/en/), s tim da DogsTrust ima razne druge stvari i novosti koje objavljuje na svojoj stranici, dok smo mi mislili da se baziramo samo na udomljavanje. Također, na DogsTrust opcija udomljavanja ide preko prijave i e-maila, dok u ovom slučaju to nije tako. Također, ova stranica neće biti bazirana samo na pse.\n" +
                "Samim tim, pronađena je jedna stranica koja vrši sličnu stvar kao naša, a zove se Rehome (https://rehome.adoptapet.com). Ova stranica udomljava životinje online, na sličan način kao što će to biti urađeno ovdje, s tim da ovdje neće biti opcije upoznavanja budućih vlasnika i vršenja procesa udomljavanja od strane korisnika koji je prijavio životinju na udomljavanje, nego će to biti posao administracije. Druga stranica koja je namijenja samo za udomljavanje životinja jeste The Shelter Pet Project (https://theshelterpetproject.org) i nešto slično će postojati i ona ovoj stranici, uz ostale funkcionalnosti. Dakle, može se reći da je ova naša stranica kombinacija Rehome i The Shelter Pet Project stranice.\n");
        Set<ConstraintViolation<AdoptionRequest>> violations = validator.validate(adoptionRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void messageTooLongAddPetRequest() {
        AddPetRequest addPetRequest = new AddPetRequest();
        addPetRequest.setUserID(1L);
        addPetRequest.setNewPetID(1L);
        addPetRequest.setMessage("Što se tiče lokalnih stranica, ova stranica je kao DogsTrust (http://www.dogstrust.ba/en/), s tim da DogsTrust ima razne druge stvari i novosti koje objavljuje na svojoj stranici, dok smo mi mislili da se baziramo samo na udomljavanje. Također, na DogsTrust opcija udomljavanja ide preko prijave i e-maila, dok u ovom slučaju to nije tako. Također, ova stranica neće biti bazirana samo na pse.\n" +
                "Samim tim, pronađena je jedna stranica koja vrši sličnu stvar kao naša, a zove se Rehome (https://rehome.adoptapet.com). Ova stranica udomljava životinje online, na sličan način kao što će to biti urađeno ovdje, s tim da ovdje neće biti opcije upoznavanja budućih vlasnika i vršenja procesa udomljavanja od strane korisnika koji je prijavio životinju na udomljavanje, nego će to biti posao administracije. Druga stranica koja je namijenja samo za udomljavanje životinja jeste The Shelter Pet Project (https://theshelterpetproject.org) i nešto slično će postojati i ona ovoj stranici, uz ostale funkcionalnosti. Dakle, može se reći da je ova naša stranica kombinacija Rehome i The Shelter Pet Project stranice.\n");
        Set<ConstraintViolation<AddPetRequest>> violations = validator.validate(addPetRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void approvedIsFalseByDefaultAdoptionRequest() {
        AdoptionRequest adoptionRequest = new AdoptionRequest();
        adoptionRequest.setUserID(1L);
        adoptionRequest.setPetID(1L);
        assertFalse(adoptionRequest.isApproved());
    }

    @Test
    public void approvedIsFalseByDefaultAddPetRequest() {
        AddPetRequest addPetRequest = new AddPetRequest();
        addPetRequest.setUserID(1L);
        addPetRequest.setNewPetID(1L);
        assertFalse(addPetRequest.isApproved());
    }

    @Test
    public void validAdoptionRequest() {
        AdoptionRequest adoptionRequest = new AdoptionRequest();
        adoptionRequest.setPetID(1L);
        adoptionRequest.setUserID(1L);
        Set<ConstraintViolation<AdoptionRequest>> violations = validator.validate(adoptionRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void validAddPetRequest() {
        AddPetRequest addPetRequest = new AddPetRequest();
        addPetRequest.setNewPetID(1L);
        addPetRequest.setUserID(1L);
        Set<ConstraintViolation<AddPetRequest>> violations = validator.validate(addPetRequest);
        assertTrue(violations.isEmpty());
    }
}
