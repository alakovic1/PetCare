package ba.unsa.etf.nwt.system_events_service.repository;

import ba.unsa.etf.nwt.system_events_service.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
}
