package ba.unsa.etf.nwt.user_service.request.password_requests;

import ba.unsa.etf.nwt.user_service.model.Answer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordAnswerRequest {
    @NotBlank(message = "Email can't be blank")
    @Size(max = 100, message = "Emails max length is 100")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Answer can't be null")
    private Answer answer;

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
}
