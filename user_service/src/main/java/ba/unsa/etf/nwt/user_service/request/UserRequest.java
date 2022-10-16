package ba.unsa.etf.nwt.user_service.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "Email can't be blank")
    @Size(max = 100, message = "Emails max length is 100")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password can't be blank")
    //@Size(min = 6, max = 40, message = "Passwords min length is 6, max length is 40")
    //@PasswordValidation
    private String password;

    public UserRequest(@NotBlank @Size(max = 100) @Email(message = "Email should be valid") String email, @NotBlank @Size(min = 6, max = 40) String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
