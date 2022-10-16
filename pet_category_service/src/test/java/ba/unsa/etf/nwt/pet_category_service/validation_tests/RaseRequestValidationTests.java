package ba.unsa.etf.nwt.pet_category_service.validation_tests;


import ba.unsa.etf.nwt.pet_category_service.request.RaseRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class RaseRequestValidationTests {

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
    public void testRaseNoAttributeValue(){
        RaseRequest r = new RaseRequest();
        Set<ConstraintViolation<RaseRequest>> violations = validator.validate(r);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankCategoryID(){
        RaseRequest r = new RaseRequest();
        r.setName("fsdf");
        Set<ConstraintViolation<RaseRequest>> violations = validator.validate(r);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNameSizeTooBig(){
        RaseRequest r = new RaseRequest();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<70;i++){
            sb.append('x');
        }
        r.setName(sb.toString());

        Set<ConstraintViolation<RaseRequest>> violations = validator.validate(r);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankRaseName(){
        RaseRequest r = new RaseRequest();
        r.setName("");
        Set<ConstraintViolation<RaseRequest>> violations = validator.validate(r);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankRaseDescription(){
        RaseRequest r = new RaseRequest();
        r.setName("name");
        r.setDescription("");
        Set<ConstraintViolation<RaseRequest>> violations = validator.validate(r);
        assertFalse(violations.isEmpty());
    }

}
