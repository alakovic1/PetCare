package ba.unsa.etf.nwt.adopt_service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PetRequest {

        @NotBlank(message = "Pet name can't be blank!")
        @Size(min = 2, max = 50, message = "Pet name must be between 2 and 50 characters!")
        private String name;

        @NotBlank(message = "Pet location can't be blank!")
        private String location;

        //@NotBlank(message = "Pet image can't be blank!")
        @Column(columnDefinition = "text")
        private String image;

        @Column(columnDefinition = "text")
        private String description;

        @NotNull(message = "Pet age can't be blank!")
        @Max(value = 100, message = "Pet can't be older than 100 years!")
        private Integer age;

        private Long rase_id;

        public String getName() {
            return name;
        }

        public void setName(@NotNull String name) {
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

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge( Integer age) {
            this.age = age;
        }

        public Long getRase_id() {
            return rase_id;
        }

        public void setRase_id(Long rase_id) {
            this.rase_id = rase_id;
        }

}
