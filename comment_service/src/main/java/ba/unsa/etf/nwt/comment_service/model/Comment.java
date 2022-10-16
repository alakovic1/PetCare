package ba.unsa.etf.nwt.comment_service.model;

import ba.unsa.etf.nwt.comment_service.model.sectionRole.MainRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private Long categoryID;

    @NotBlank(message = "Title can't be blank")
    @Size(min = 2, max = 100, message
            = "Title must be between 2 and 100 characters!!")
    @Column(columnDefinition = "text")
    private String title;

    @NotBlank(message = "Content can't be blank")
    @Size(min = 2, max = 1000, message
            = "Content must be between 2 and 1000 characters!!")
    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "main_role_id", nullable = false)
    private MainRole mainRole;

    public Comment(Comment comment) {
        this.mainRole = comment.mainRole;
        this.content = comment.content;
        this.username = comment.username;
        this.categoryID = comment.categoryID;
        this.id = comment.id;
        this.title = comment.title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MainRole getRoles() {
        return mainRole;
    }

    public void setRoles(MainRole role) {
        this.mainRole = role;
    }
}
