package ba.unsa.etf.nwt.user_service.request;

import ba.unsa.etf.nwt.user_service.annotation.PasswordValidation;
import ba.unsa.etf.nwt.user_service.model.Answer;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegistrationRequest {
    @NotBlank(message = "Name can't be blank")
    @Size(min = 3, max = 50, message = "Names min length is 3, max length is 50")
    @Column(columnDefinition = "text")
    private String name;

    @NotBlank(message = "Surname can't be blank")
    @Size(min = 3, max = 50, message = "Surnames min length is 3, max length is 50")
    @Column(columnDefinition = "text")
    private String surname;

    @NotBlank(message = "Email can't be blank")
    @Size(max = 100, message = "Emails max length is 100")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Username can't be blank")
    @Size(min = 4, max = 40, message = "Usernames min length is 4, max length is 40")
    private String username;

    @NotBlank(message = "Password can't be blank")
    @Size(min = 6, max = 40, message = "Passwords min length is 6, max length is 40")
    @PasswordValidation
    private String password;

    @NotNull(message = "Answer can't be null")
    private Answer answer;

    public RegistrationRequest(@NotBlank(message = "Name can't be blank") @Size(min = 3, max = 50, message = "Names min length is 3, max length is 50") String name, @NotBlank(message = "Surname can't be blank") @Size(min = 3, max = 50, message = "Surnames min length is 3, max length is 50") String surname, @NotBlank(message = "Email can't be blank") @Size(max = 100, message = "Emails max length is 100") @Email(message = "Email should be valid") String email, @NotBlank(message = "Username can't be blank") @Size(min = 4, max = 40, message = "Usernames min length is 4, max length is 40") String username, @NotBlank(message = "Password can't be blank") @Size(min = 6, max = 40, message = "Passwords min length is 6, max length is 40") String password, @NotNull(message = "Answer can't be null") Answer answer) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.answer = answer;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
