package ba.unsa.etf.nwt.pet_category_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //@JsonIgnore
    private Long id;

    @NotBlank(message = "Pet name can't be blank!")
    @Size(min = 2, max = 50, message = "Pet name must be between 2 and 50 characters!")
    private String name;

    @NotBlank(message = "Pet location can't be blank!")
    private String location;

    //da li je image string? jeste..
    //@NotBlank(message = "Pet image can't be blank!")
    @Column(columnDefinition = "text")
    private String image;

    @Column(columnDefinition = "text")
    private String description;

    @NotNull(message = "Pet age can't be blank!")
    @Max(value = 100, message = "Pet can't be older than 100 years!")
    private Integer age;

    private boolean approved = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rase_id")
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Rase rase;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descrtiption) {
        this.description = descrtiption;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(@NotNull Integer age) {
        this.age = age;
    }

    public Rase getRase() {
        return rase;
    }

    public void setRase(Rase rase) {
        this.rase = rase;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
