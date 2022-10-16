package ba.unsa.etf.nwt.user_service.request;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserProfileRequest {

    @NotBlank(message = "Name can't be blank")
    @Size(min = 3, max = 50, message = "Names min length is 3, max length is 50")
    @Column(columnDefinition = "text")
    private String name;

    @NotBlank(message = "Surname can't be blank")
    @Size(min = 3, max = 50, message = "Surnames min length is 3, max length is 50")
    @Column(columnDefinition = "text")
    private String surname;

    @NotBlank(message = "Username can't be blank")
    @Size(min = 4, max = 40, message = "Usernames min length is 4, max length is 40")
    private String username;

    @NotBlank(message = "Email can't be blank")
    @Size(max = 100, message = "Emails max length is 100")
    @Email(message = "Email should be valid")
    private String email;

    public UserProfileRequest(@NotBlank(message = "Name can't be blank") @Size(min = 3, max = 50, message = "Names min length is 3, max length is 50") String name, @NotBlank(message = "Surname can't be blank") @Size(min = 3, max = 50, message = "Surnames min length is 3, max length is 50") String surname, @NotBlank(message = "Username can't be blank") @Size(min = 4, max = 40, message = "Usernames min length is 4, max length is 40") String username, @NotBlank(message = "Email can't be blank") @Size(max = 100, message = "Emails max length is 100") @Email(message = "Email should be valid") String email) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
