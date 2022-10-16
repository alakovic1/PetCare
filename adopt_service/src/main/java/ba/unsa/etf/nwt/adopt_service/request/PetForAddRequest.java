package ba.unsa.etf.nwt.adopt_service.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@AllArgsConstructor
@NoArgsConstructor
public class PetForAddRequest {

    @Valid
    private PetRequest petForAdd;

    private String message;

    public PetRequest getPetForAdd() {
        return petForAdd;
    }

    public void setPetForAdd(PetRequest petForAdd) {
        this.petForAdd = petForAdd;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
