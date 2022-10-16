package ba.unsa.etf.nwt.notification_service;

import ba.unsa.etf.nwt.notification_service.model.Notification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
class ValidationNotificationServiceTests {
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
    public void nullNotification() {
        Notification notification = new Notification();
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void notificationOnlyWithUser() {
        Notification notification = new Notification();
        notification.setUserID(2L);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void notificationDiffWithUser() {
        Notification notification = new Notification();
        notification.setUserID(1L);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void notificationOnlyWithContent() {
        Notification notification = new Notification();
        notification.setContent("Error");
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void contentSmaller() {
        Notification notification = new Notification();
        notification.setContent("O");
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validContentAndUser() {
        Notification notification = new Notification();
        notification.setContent("Notif");
        notification.setUserID(1L);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void smallContentAndUser() {
        Notification notification = new Notification();
        notification.setContent("N");
        notification.setUserID(1L);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void smallContentUserAndRead() {
        Notification notification = new Notification();
        notification.setContent("N");
        notification.setUserID(1L);
        notification.setRead(false);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void smallContentUserAndRead2() {
        Notification notification = new Notification();
        notification.setContent("N");
        notification.setUserID(1L);
        notification.setRead(true);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void contentUserAndRead2() {
        Notification notification = new Notification();
        notification.setContent("Notif");
        notification.setUserID(1L);
        notification.setRead(true);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void contentUserAndRead() {
        Notification notification = new Notification();
        notification.setContent("Notif");
        notification.setUserID(1L);
        notification.setRead(false);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void smallContentAndRead2() {
        Notification notification = new Notification();
        notification.setContent("N");
        notification.setRead(true);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void smallContentAndRead() {
        Notification notification = new Notification();
        notification.setContent("N");
        notification.setRead(false);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void contentAndRead() {
        Notification notification = new Notification();
        notification.setContent("Notification");
        notification.setRead(false);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void contentAndRead2() {
        Notification notification = new Notification();
        notification.setContent("Notification");
        notification.setRead(true);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void smallContentWithTime() {
        Notification notification = new Notification();
        notification.setContent("C");
        notification.setUserID(1L);
        notification.setRead(false);
        notification.setCreatedAt(new Date());
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void contentWithTimeNoUser() {
        Notification notification = new Notification();
        notification.setContent("Content");
        notification.setRead(false);
        notification.setCreatedAt(new Date());
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void timeWithNoContent() {
        Notification notification = new Notification();
        notification.setUserID(1L);
        notification.setRead(false);
        notification.setCreatedAt(new Date());
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void smallContentWithTimeNoUser() {
        Notification notification = new Notification();
        notification.setContent("C");
        notification.setRead(false);
        notification.setCreatedAt(new Date());
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void readAndCreated() {
        Notification notification = new Notification();
        notification.setRead(false);
        notification.setCreatedAt(new Date());
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void readAndCreated2() {
        Notification notification = new Notification();
        notification.setRead(true);
        notification.setCreatedAt(new Date());
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void onlyCreated() {
        Notification notification = new Notification();
        notification.setCreatedAt(new Date());
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void onlyRead() {
        Notification notification = new Notification();
        notification.setRead(true);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void onlyRead2() {
        Notification notification = new Notification();
        notification.setRead(false);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void correctPartNotification() {
        Notification notification = new Notification();
        notification.setContent("Correct");
        notification.setUserID(1L);
        notification.setRead(false);
        notification.setCreatedAt(new Date());
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void onlyIsForAdminTrue() {
        Notification notification = new Notification();
        notification.setForAdmin(true);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void onlyIsForAdminFalse() {
        Notification notification = new Notification();
        notification.setForAdmin(false);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isAddPetRequestAndRequestId1() {
        Notification notification = new Notification();
        notification.setIsAddPetRequest(true);
        notification.setRequestId(-1L);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isAddPetRequestAndRequestId2() {
        Notification notification = new Notification();
        notification.setIsAddPetRequest(true);
        notification.setRequestId(1L);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isAddPetRequestAndRequestId3() {
        Notification notification = new Notification();
        notification.setIsAddPetRequest(false);
        notification.setRequestId(2L);
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void almostFullNotification() {
        Notification notification = new Notification();
        notification.setContent("Correct");
        notification.setUserID(1L);
        notification.setRead(false);
        notification.setIsAddPetRequest(true);
        notification.setRequestId(2L);
        notification.setCreatedAt(new Date());
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void FullNotification() {
        Notification notification = new Notification();
        notification.setContent("Correct");
        notification.setUserID(1L);
        notification.setRead(false);
        notification.setForAdmin(true);
        notification.setIsAddPetRequest(false);
        notification.setRequestId(1L);
        notification.setCreatedAt(new Date());
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertTrue(violations.isEmpty());
    }
}
