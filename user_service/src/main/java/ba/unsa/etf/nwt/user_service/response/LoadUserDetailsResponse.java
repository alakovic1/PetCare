package ba.unsa.etf.nwt.user_service.response;

import ba.unsa.etf.nwt.user_service.model.roles.Role;

import java.util.HashSet;
import java.util.Set;

public class LoadUserDetailsResponse {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String username;

    private String password;

    private Set<Role> roles = new HashSet<>();

    public LoadUserDetailsResponse() {
    }

    public LoadUserDetailsResponse(Long id, String name, String surname, String email, String username, String password, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
