package ba.unsa.etf.nwt.pet_category_service.validation_tests;

import ba.unsa.etf.nwt.pet_category_service.model.Rase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class RaseValidationTests {
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
        Rase r = new Rase();
        Set<ConstraintViolation<Rase>> violations = validator.validate(r);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankRaseName(){
        Rase r = new Rase();
        r.setName("");
        Set<ConstraintViolation<Rase>> violations = validator.validate(r);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNameSizeTooBig(){
        Rase r = new Rase();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<70;i++){
            sb.append('x');
        }
        r.setName(sb.toString());

        Set<ConstraintViolation<Rase>> violations = validator.validate(r);
        assertFalse(violations.isEmpty());
    }

}
