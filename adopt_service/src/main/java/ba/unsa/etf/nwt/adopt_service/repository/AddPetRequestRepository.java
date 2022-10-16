package ba.unsa.etf.nwt.adopt_service.repository;

import ba.unsa.etf.nwt.adopt_service.model.AddPetRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddPetRequestRepository extends JpaRepository<AddPetRequest, Long> {
    List<AddPetRequest> findAllByNewPetID(Long newPetId);
}
