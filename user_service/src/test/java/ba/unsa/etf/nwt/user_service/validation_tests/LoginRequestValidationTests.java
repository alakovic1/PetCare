package ba.unsa.etf.nwt.user_service.validation_tests;

import ba.unsa.etf.nwt.user_service.request.LoginRequest;
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
class LoginRequestValidationTests {
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
    public void testBlankUsernameOrEmail(){
        LoginRequest loginRequest = new LoginRequest("", "");
        loginRequest.setUsernameOrEmail("");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNoUsernameOrEmail(){
        LoginRequest loginRequest = new LoginRequest("", "");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBigUsernameOrEmail(){
        LoginRequest loginRequest = new LoginRequest("", "");

        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 105; i++){
            string.append("a");
        }

        loginRequest.setUsernameOrEmail(string.toString());
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankPassword(){
        LoginRequest loginRequest = new LoginRequest("", "");
        loginRequest.setPassword("");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNoPassword(){
        LoginRequest loginRequest = new LoginRequest("", "");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testSmallPassword(){
        LoginRequest loginRequest = new LoginRequest("", "");
        loginRequest.setPassword("a");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBigPassword(){
        LoginRequest loginRequest = new LoginRequest("", "");

        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 105; i++){
            string.append("a");
        }

        loginRequest.setPassword(string.toString());
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty());
    }

}
