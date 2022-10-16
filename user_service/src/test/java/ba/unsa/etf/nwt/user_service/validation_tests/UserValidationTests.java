package ba.unsa.etf.nwt.user_service.validation_tests;

import ba.unsa.etf.nwt.user_service.model.User;
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
class UserValidationTests {
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
    public void testBlankUsersName(){
        User u = new User();
        u.setName("");
        u.setPassword("pass");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNoUsersName(){
        User u = new User();
        u.setPassword("pass");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBigUsersName(){
        User u = new User();

        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 105; i++){
            string.append("a");
        }

        u.setName(string.toString());
        u.setPassword("pass");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankUsersSurname(){
        User u = new User();
        u.setSurname("");
        u.setPassword("pass");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNoUsersSurname(){
        User u = new User();
        u.setPassword("pass");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBigUserSurname(){
        User u = new User();

        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 105; i++){
            string.append("a");
        }

        u.setSurname(string.toString());
        u.setPassword("pass");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankEmail(){
        User u = new User();
        u.setEmail("");
        u.setPassword("pass");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNoEmail(){
        User u = new User();
        u.setPassword("pass");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBigEmail(){
        User u = new User();

        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 105; i++){
            string.append("a");
        }

        u.setEmail(string.toString());
        u.setPassword("pass");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankUsersUsername(){
        User u = new User();
        u.setUsername("");
        u.setPassword("pass");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNoUsersUsername(){
        User u = new User();
        u.setPassword("pass");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBigUserUsername(){
        User u = new User();

        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 105; i++){
            string.append("a");
        }

        u.setUsername(string.toString());
        u.setPassword("pass");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankUsersPassword(){
        User u = new User();
        u.setPassword("");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNoUsersPassword(){
        User u = new User();
        u.setPassword("pass");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBigUsersPassword(){
        User u = new User();

        StringBuilder string = new StringBuilder("a");

        for(int i = 0; i <= 105; i++){
            string.append("a");
        }

        u.setPassword(string.toString());
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertFalse(violations.isEmpty());
    }

}
