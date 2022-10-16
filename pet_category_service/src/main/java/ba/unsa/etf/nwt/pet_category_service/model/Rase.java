package ba.unsa.etf.nwt.pet_category_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rase")
public class Rase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //@JsonIgnore
    private Long id;

    @NotBlank(message = "Rase name can't be blank!")
    @Size(min = 2, max = 50, message = "Rase name must be between 2 and 50 characters!")
    private String name;

    //@NotBlank(message = "Rase description can't be blank!")
    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
