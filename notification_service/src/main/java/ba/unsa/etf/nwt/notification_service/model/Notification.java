package ba.unsa.etf.nwt.notification_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonIgnore
    private Long id;

    @NotBlank(message = "Content can't be blank!!")
    @Column(columnDefinition = "text")
    @Size(min = 2, max = 150, message
            = "Content must be between 2 and 150 characters!!")
    private String content;

    private Long userID = -1L;

    private Boolean read = false;

    private Boolean isForAdmin = true;

    private Boolean isAddPetRequest = true;

    private Long requestId = -1L;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(timezone="Europe/Sarajevo")
    @Column(name = "created_at", nullable = true, updatable = false)
    @CreatedDate
    private Date createdAt;

    public Notification(@NotBlank(message = "Content can't be blank!!") @Size(min = 2, max = 150, message
            = "Content must be between 2 and 150 characters!!") String content, Long userID, Boolean read, Boolean isForAdmin, Boolean isAddPetRequest, Long requestId, Date createdAt) {
        this.content = content;
        this.userID = userID;
        this.read = read;
        this.isForAdmin = isForAdmin;
        this.isAddPetRequest = isAddPetRequest;
        this.requestId = requestId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getForAdmin() {
        return isForAdmin;
    }

    public void setForAdmin(Boolean forAdmin) {
        isForAdmin = forAdmin;
    }

    public Boolean getAddPetRequest() {
        return isAddPetRequest;
    }

    public void setAddPetRequest(Boolean addPetRequest) {
        isAddPetRequest = addPetRequest;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
}
