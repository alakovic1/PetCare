package ba.unsa.etf.nwt.pet_category_service.repository;

import ba.unsa.etf.nwt.pet_category_service.model.Rase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RaseRepository extends JpaRepository<Rase, Long> {
    Optional<Rase> findByName(String name);

    List<Rase> findByCategory_Id(Long id);
}
