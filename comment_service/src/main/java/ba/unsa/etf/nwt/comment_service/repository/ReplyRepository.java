package ba.unsa.etf.nwt.comment_service.repository;

import ba.unsa.etf.nwt.comment_service.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findAllByUsername(String username);

}
