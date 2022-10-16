package ba.unsa.etf.nwt.comment_service.service;


import ba.unsa.etf.nwt.comment_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.comment_service.exception.WrongInputException;
import ba.unsa.etf.nwt.comment_service.model.Comment;
import ba.unsa.etf.nwt.comment_service.model.Reply;
import ba.unsa.etf.nwt.comment_service.repository.ReplyRepository;
import ba.unsa.etf.nwt.comment_service.response.ResponseMessage;
import ba.unsa.etf.nwt.comment_service.security.CurrentUser;
import ba.unsa.etf.nwt.comment_service.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentService commentService;
    private final CommunicationsService communicationsService;

    public void setUsernameOnUnknown(String username){
        List<Reply> replies = replyRepository.findAllByUsername(username);
        for(Reply reply : replies){
            reply.setUsername("UNKNOWN");
            replyRepository.save(reply);
        }
    }


    public List<Reply> getReply() {
        /*try {
            RestTemplate restTemplate = new RestTemplate();
            List<Reply> replies = replyRepository.findAll();
            for (Reply reply : replies) {
                String username = restTemplate.getForObject(communicationsService.getUri("user_service") +
                        "/user/" + reply.getUsername(), String.class);
                reply.setUsername(username);
                replyRepository.save(reply);
            }
            return replies;
        } catch (Exception e){
            throw new ResourceNotFoundException ("Can't connect to user_service!");
        }*/

        return replyRepository.findAll();
    }

    public ResponseMessage addReply(Reply reply, Long commentId, @CurrentUser UserPrincipal currentUser) {

        //RestTemplate restTemplate = new RestTemplate();

        try {
            reply.setComment(new Comment(commentService.getOneComment(commentId)));

            /*String username = restTemplate.getForObject(communicationsService.getUri("user_service") +
                    "/user/me/username", String.class);*/

            reply.setUsername(currentUser.getUsername());
            replyRepository.save(reply);
            return new ResponseMessage(true, HttpStatus.OK,"Reply added successfully!!");
        } catch (RuntimeException re){
            throw new WrongInputException("Reply isn't added!!");
        }
        /*catch (Exception e){
            throw new ResourceNotFoundException("Can't connect to user_service!!");
        }*/
    }

    public List<Reply> getAllReplyForComment(Long commentID) {
        return replyRepository
                .findAll()
                .stream()
                .filter(r -> r.getComment().getId().equals(commentID))
                .collect(Collectors.toList());
    }

    public ResponseMessage updateReply(Reply reply, Long replyID) {
        try {
            Reply oldReply = replyRepository
                    .findAll()
                    .stream()
                    .filter(c -> c.getId().equals(replyID))
                    .collect(Collectors.toList()).get(0);
            oldReply.setContent(reply.getContent());
            replyRepository.save(oldReply);
            return new ResponseMessage(true, HttpStatus.OK, "Reply updated successfully!!");
        }
        catch (RuntimeException e){
            throw new WrongInputException("Reply isn't updated!!");
        }
    }

    public ResponseMessage deleteReply(Long replyID) {
        try {
            replyRepository.deleteById(replyID);
            return new ResponseMessage(true, HttpStatus.OK,"Reply deleted successfully!!");
        } catch (Exception e) {
            throw new ResourceNotFoundException( "Reply isn't deleted!!");
        }
    }

    public Reply saveReply(Reply r){
        return replyRepository.save(r);
    }

    public Optional<Reply> findById(Long id){
        return replyRepository.findById(id);
    }
}
