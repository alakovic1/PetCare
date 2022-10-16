package ba.unsa.etf.nwt.user_service.validation_tests;

import ba.unsa.etf.nwt.user_service.model.Answer;
import ba.unsa.etf.nwt.user_service.request.RegistrationRequest;
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
class RegistrationValidationTests {
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
    public void testBlankName(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        registrationRequest.setName("");
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNoName(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testSmallName(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        registrationRequest.setName("a");
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBigName(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());

        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 55; i++){
            string.append("a");
        }

        registrationRequest.setName(string.toString());
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankSurname(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        registrationRequest.setSurname("");
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNoSurname(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testSmallSurname(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        registrationRequest.setSurname("a");
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBigSurname(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());

        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 55; i++){
            string.append("a");
        }

        registrationRequest.setSurname(string.toString());
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankEmail(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        registrationRequest.setEmail("");
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNoEmail(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBigEmail(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());

        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 105; i++){
            string.append("a");
        }

        registrationRequest.setEmail(string.toString());
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankUsername(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        registrationRequest.setUsername("");
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNoUsername(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testSmallUsername(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        registrationRequest.setUsername("a");
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBigUsername(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());

        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 45; i++){
            string.append("a");
        }

        registrationRequest.setUsername(string.toString());
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankPassword(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        registrationRequest.setPassword("");
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNoPassword(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testSmallPassword(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());
        registrationRequest.setPassword("a");
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBigPassword(){
        RegistrationRequest registrationRequest = new RegistrationRequest("", "", "", "", "", new Answer());

        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 45; i++){
            string.append("a");
        }

        registrationRequest.setPassword(string.toString());
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        assertFalse(violations.isEmpty());
    }

}
