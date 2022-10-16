package ba.unsa.etf.nwt.user_service.repository;

import ba.unsa.etf.nwt.user_service.model.InvalidTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidTokensRepository extends JpaRepository<InvalidTokens, Long> {
}
