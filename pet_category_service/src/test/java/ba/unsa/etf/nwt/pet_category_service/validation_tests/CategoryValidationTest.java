package ba.unsa.etf.nwt.pet_category_service.validation_tests;

import ba.unsa.etf.nwt.pet_category_service.model.Category;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class CategoryValidationTest {

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
    public void testCategoryNoAttributeValue(){
        Category c = new Category();
        Set<ConstraintViolation<Category>> violations = validator.validate(c);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankCategoryName(){
        Category c = new Category();
        c.setName("");
        Set<ConstraintViolation<Category>> violations = validator.validate(c);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNameSizeTooBig(){
        Category c = new Category();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<70;i++){
            sb.append('x');
        }
        c.setName(sb.toString());

        Set<ConstraintViolation<Category>> violations = validator.validate(c);
        assertFalse(violations.isEmpty());
    }

}
