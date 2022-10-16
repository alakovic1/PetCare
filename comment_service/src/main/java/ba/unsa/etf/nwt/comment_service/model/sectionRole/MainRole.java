package ba.unsa.etf.nwt.comment_service.model.sectionRole;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@Table(name = "main_role")
public class MainRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    @NotNull(message = "Role name cannot be null")
    private SectionRoleName name;

    public MainRole() {
    }

    public MainRole(SectionRoleName sectionRoleName) {
        this.name = sectionRoleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SectionRoleName getName() {
        return name;
    }

    public void setName(SectionRoleName name) {
        this.name = name;
    }
}
