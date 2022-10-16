package ba.unsa.etf.nwt.user_service.validation_tests;

import ba.unsa.etf.nwt.user_service.model.Question;
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
class QuestionValidationTests {
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
    public void testBlankQuestionTitle(){
        Question q = new Question();
        q.setTitle("");
        Set<ConstraintViolation<Question>> violations = validator.validate(q);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testQuestionNoTitle(){
        Question q = new Question();
        Set<ConstraintViolation<Question>> violations = validator.validate(q);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankQuestionDescription(){
        Question q = new Question();
        q.setDescription("");
        Set<ConstraintViolation<Question>> violations = validator.validate(q);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testQuestionNoDescription(){
        Question q = new Question();
        Set<ConstraintViolation<Question>> violations = validator.validate(q);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testQuestionTooBigTitle(){
        Question q = new Question();
        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 105; i++){
            string.append("a");
        }

        q.setTitle(string.toString());
        Set<ConstraintViolation<Question>> violations = validator.validate(q);
        assertFalse(violations.isEmpty());
    }
}
