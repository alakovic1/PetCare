package ba.unsa.etf.nwt.user_service.response;

public class UserProfileResponse {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String role;

    public UserProfileResponse(String name, String surname, String username, String email, String role) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
