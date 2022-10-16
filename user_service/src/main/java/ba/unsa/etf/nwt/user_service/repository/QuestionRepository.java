package ba.unsa.etf.nwt.user_service.repository;

import ba.unsa.etf.nwt.user_service.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByTitle(String title);
}
