package ba.unsa.etf.nwt.adopt_service.repository;

import ba.unsa.etf.nwt.adopt_service.model.AdoptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, Long> {
    List<AdoptionRequest> findAllByApprovedAndPetID(Boolean isApproved, Long id);

    List<AdoptionRequest> findAllByPetID(Long petId);
}
