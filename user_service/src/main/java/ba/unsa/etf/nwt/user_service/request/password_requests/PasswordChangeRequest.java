package ba.unsa.etf.nwt.user_service.request.password_requests;

import ba.unsa.etf.nwt.user_service.annotation.PasswordValidation;
import ba.unsa.etf.nwt.user_service.model.Answer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordChangeRequest {
    @NotBlank(message = "Email can't be blank")
    @Size(max = 100, message = "Emails max length is 100")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Answer can't be null")
    private Answer answer;

    @NotBlank(message = "Password can't be blank")
    //@Size(min = 6, max = 40, message = "Passwords min length is 6, max length is 40")
    //@PasswordValidation
    private String oldPassword;

    @NotBlank(message = "Password can't be blank")
    @Size(min = 6, max = 40, message = "Passwords min length is 6, max length is 40")
    @PasswordValidation
    private String newPassword;

    public PasswordChangeRequest(@NotBlank(message = "Email can't be blank") @Size(max = 100, message = "Emails max length is 100") @Email(message = "Email should be valid") String email, @NotNull(message = "Answer can't be null") Answer answer, @NotBlank(message = "Password can't be blank") String oldPassword, @NotBlank(message = "Password can't be blank") @Size(min = 6, max = 40, message = "Passwords min length is 6, max length is 40") String newPassword) {
        this.email = email;
        this.answer = answer;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
