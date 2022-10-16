package ba.unsa.etf.nwt.user_service.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValidation {
    String message() default "Password not valid (at least 6 characters, 1 big letter, 1 small letter, 1 sign)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
