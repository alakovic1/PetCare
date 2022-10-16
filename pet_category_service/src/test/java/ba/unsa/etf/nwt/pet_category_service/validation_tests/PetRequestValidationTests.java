package ba.unsa.etf.nwt.pet_category_service.validation_tests;

import ba.unsa.etf.nwt.pet_category_service.request.PetRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PetRequestValidationTests {

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
    public void testPetRequestNoAttributeValue(){
        PetRequest p = new PetRequest();
        Set<ConstraintViolation<PetRequest>> violations = validator.validate(p);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankPetRequestName(){
        PetRequest p = new PetRequest();
        p.setName("");
        p.setLocation("aaaa");
        //p.setAdopted(false);
        p.setImage("xxx");
        p.setAge(2);
        Set<ConstraintViolation<PetRequest>> violations = validator.validate(p);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankPetRequestLocation(){
        PetRequest p = new PetRequest();
        p.setName("aaa");
        p.setLocation("");
        //p.setAdopted(false);
        p.setImage("xxx");
        p.setAge(2);
        Set<ConstraintViolation<PetRequest>> violations = validator.validate(p);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankPetImage(){
        PetRequest p = new PetRequest();
        p.setName("aaa");
        p.setLocation("aaaa");
        p.setImage("");
        p.setAge(2);
        Set<ConstraintViolation<PetRequest>> violations = validator.validate(p);
        assertTrue(violations.isEmpty());
    }


    @Test
    public void testBlankPetAge(){
        PetRequest p = new PetRequest();
        p.setName("aaa");
        p.setLocation("aaaa");
        p.setImage("hghj");
        Set<ConstraintViolation<PetRequest>> violations = validator.validate(p);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testPetAgeOverMax(){
        PetRequest p = new PetRequest();
        p.setName("aaa");
        p.setLocation("aaaa");
        p.setImage("hhh");
        p.setAge(150);
        Set<ConstraintViolation<PetRequest>> violations = validator.validate(p);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testPetNameTooShort(){
        PetRequest p = new PetRequest();
        p.setName("a");
        p.setLocation("aaaa");
        p.setImage("hhh");
        p.setAge(50);
        Set<ConstraintViolation<PetRequest>> violations = validator.validate(p);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNameSizeTooBig(){
        PetRequest p = new PetRequest();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<70;i++){
            sb.append('x');
        }
        p.setName(sb.toString());
        p.setLocation("aaaa");
        p.setImage("");
        p.setAge(2);
        Set<ConstraintViolation<PetRequest>> violations = validator.validate(p);
        assertFalse(violations.isEmpty());
    }
}
