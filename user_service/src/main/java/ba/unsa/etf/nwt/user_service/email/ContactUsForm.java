package ba.unsa.etf.nwt.user_service.email;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ContactUsForm {
    @NotBlank(message = "Name can't be blank")
    private String name;

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Feedback can't be blank")
    @Size(min = 5, message = "Minimum length of the feedback is 5")
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
