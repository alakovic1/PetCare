package ba.unsa.etf.nwt.user_service.validation_tests;

import ba.unsa.etf.nwt.user_service.model.Answer;
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

@ActiveProfiles("test")
class AnswerValidationTests {
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
    public void testBlankAnswerText(){
        Answer a = new Answer();
        a.setText("");
        Set<ConstraintViolation<Answer>> violations = validator.validate(a);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testAnswerNoText(){
        Answer a = new Answer();
        Set<ConstraintViolation<Answer>> violations = validator.validate(a);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testAnswerBigText(){
        Answer a = new Answer();
        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 105; i++){
            string.append("a");
        }

        a.setText(string.toString());
        Set<ConstraintViolation<Answer>> violations = validator.validate(a);
        assertFalse(violations.isEmpty());
    }
}
