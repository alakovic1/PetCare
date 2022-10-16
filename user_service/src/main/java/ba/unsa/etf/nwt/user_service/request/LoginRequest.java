package ba.unsa.etf.nwt.user_service.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "Username/Email can't be blank")
    @Size(max = 100, message = "Usernames/Emails max length is 100")
    private String usernameOrEmail;

    @NotBlank(message = "Password can't be blank")
    //@Size(min = 6, max = 40, message = "Passwords min length is 6, max length is 40")
    //@PasswordValidation
    private String password;

    public LoginRequest(@NotBlank(message = "Username/Email can't be blank") @Size(max = 100, message = "Usernames/Emails max length is 100") String usernameOrEmail, @NotBlank(message = "Password can't be blank") @Size(min = 6, max = 40, message = "Passwords min length is 6, max length is 40") String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
