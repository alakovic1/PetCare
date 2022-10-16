package ba.unsa.etf.nwt.comment_service.repository;

import ba.unsa.etf.nwt.comment_service.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository <Comment, Long> {

    List<Comment> findAllByUsername(String username);

}