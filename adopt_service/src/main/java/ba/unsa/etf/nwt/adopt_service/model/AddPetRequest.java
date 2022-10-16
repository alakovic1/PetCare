package ba.unsa.etf.nwt.adopt_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "add_pet_request")
public class AddPetRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonIgnore
    private Long id;

    @NotNull(message = "User ID can't be null.")
    private Long userID;

    @NotNull(message = "New pet ID can't be null.")
    private Long newPetID;

    @Size(max = 1000, message = "Request message can't have more than 1000 characters.")
    private String message;

    private boolean approved = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Long getNewPetID() {
        return newPetID;
    }

    public void setNewPetID(Long newPetID) {
        this.newPetID = newPetID;
    }
}
