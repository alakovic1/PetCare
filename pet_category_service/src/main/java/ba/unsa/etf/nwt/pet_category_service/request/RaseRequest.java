package ba.unsa.etf.nwt.pet_category_service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaseRequest {

    @NotBlank(message = "Rase name can't be blank!")
    @Size(min = 2, max = 50, message = "Rase name must be between 2 and 50 characters!")
    private String name;

    @NotBlank(message = "Rase description can't be blank!")
    @Column(columnDefinition = "text")
    private String description;


    @NotNull(message = "ID for rase caregory can't be blank")
    private Long category_id;

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

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }
}
