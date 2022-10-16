package ba.unsa.etf.nwt.pet_category_service.repository;

import ba.unsa.etf.nwt.pet_category_service.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    //Optional<Pet> findByName(String name);
    List<Pet> findByRase_Id(Long id);

    List<Pet> findByRase_IdAndApproved(Long id, Boolean isApproved);

    List<Pet> findByRase_Category_IdAndApproved(Long id, Boolean isApproved);

    List<Pet> findByNameContainsAndApproved(String substring, Boolean isApproved);

    List<Pet> findByRase_NameContainsAndApproved(String substring, Boolean isApproved);

    List<Pet> findPetsByNameAndApproved(String name, Boolean isApproved);

    List<Pet> findByApproved(Boolean isApproved);

    Optional<Pet> findByIdAndApproved(Long id, Boolean isApproved);
}
