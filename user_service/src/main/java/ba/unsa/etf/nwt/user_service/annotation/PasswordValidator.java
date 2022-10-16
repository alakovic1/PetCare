package ba.unsa.etf.nwt.user_service.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {
    public void initialize(PasswordValidation constraint) {
    }

    public boolean isValid(String number, ConstraintValidatorContext context) {
        return number.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$");
    }
}
