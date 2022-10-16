package ba.unsa.etf.nwt.comment_service.service;

import ba.unsa.etf.nwt.comment_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.comment_service.exception.WrongInputException;
import ba.unsa.etf.nwt.comment_service.model.Comment;

import ba.unsa.etf.nwt.comment_service.model.sectionRole.SectionRoleName;
import ba.unsa.etf.nwt.comment_service.repository.CommentRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final MainRoleService mainRoleService;
    private final CommunicationsService communicationsService;

    public void setUsernameOnUnknown(String username){
        List<Comment> comments = commentRepository.findAllByUsername(username);
        for(Comment comment : comments){
            comment.setUsername("UNKNOWN");
            commentRepository.save(comment);
        }
    }

    public List<Comment> getComment() {
        /*try {
            RestTemplate restTemplate = new RestTemplate();
            List<Comment> comments = commentRepository.findAll();
            for(Comment comment : comments){
                String username = restTemplate.getForObject(communicationsService.getUri("user_service")
                        + "/user/" + comment.getUsername(), String.class);
                comment.setUsername(username);
                commentRepository.save(comment);
            }
            return comments;
        } catch (Exception e){
            throw new ResourceNotFoundException ("Can't connect to user_service!");
        }*/

        return commentRepository.findAll();
    }

    public ResponseMessage addComment(Comment comment, Long mainRoleId, @CurrentUser UserPrincipal currentUser) {

        RestTemplate restTemplate = new RestTemplate();
        SectionRoleName roleName = SectionRoleName.ROLE_PET;
        if (mainRoleId == 1L) roleName = SectionRoleName.ROLE_CATEGORY;
        SectionRoleName finalRoleName = roleName;

        if(mainRoleId != 1L && mainRoleId != 2L) throw new WrongInputException("Comment isn't added, wrong role!!");
        try {
            if(mainRoleId == 1L) {
                comment.setRoles(mainRoleService.getRoleByName(SectionRoleName.ROLE_CATEGORY));
            }
            else comment.setRoles(mainRoleService.getRoleByName(SectionRoleName.ROLE_PET));

            /*String username = restTemplate.getForObject(communicationsService.getUri("user_service")
                    + "/user/me/username", String.class);*/
            //comment.setUsername(username);

            comment.setUsername(currentUser.getUsername());
        } catch (RuntimeException re){
            throw new WrongInputException("Comment isn't added!!");
        }
        /*catch (Exception e){
            throw new ResourceNotFoundException("Current user not found!!");
        }*/
        try {
            if (roleName == SectionRoleName.ROLE_PET) {
                Long categoryId = restTemplate.getForObject(communicationsService.getUri("pet_category_service")
                        + "/current/pet/petID/" + comment.getCategoryID(), Long.class);
                comment.setCategoryID(categoryId);
            } else {
                Long categoryId = restTemplate.getForObject(communicationsService.getUri("pet_category_service")
                        + "/current/rase/raseID/" + comment.getCategoryID(), Long.class);
                comment.setCategoryID(categoryId);
            }
            commentRepository.save(comment);
            return new ResponseMessage(true, HttpStatus.OK,"Comment added successfully!!");
        } catch (Exception e){
            if(e.getMessage().equals("URI is not absolute")) {
                throw new ResourceNotFoundException("Can't connect to pet_category_service!!");
            }
            if(e.getMessage().contains("pet")) {
                throw new ResourceNotFoundException("No pet with ID " + comment.getCategoryID());
            }
            throw new ResourceNotFoundException("No rase with ID " + comment.getCategoryID());
        }
    }

    public List<Comment> getUserComments(String username) {
        return commentRepository
                .findAll()
                .stream()
                .filter(c -> c.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public Comment getOneComment(Long commentID) {
        return commentRepository
                .findAll()
                .stream()
                .filter(c -> c.getId().equals(commentID))
                .collect(Collectors.toList()).get(0);
    }

    public ResponseMessage updateComment(Comment comment, Long commentID) {
        try {
            Comment oldComment = commentRepository
                    .findAll()
                    .stream()
                    .filter(c -> c.getId().equals(commentID))
                    .collect(Collectors.toList()).get(0);
            oldComment.setTitle(comment.getTitle());
            oldComment.setContent(comment.getContent());
            commentRepository.save(oldComment);
            return new ResponseMessage(true, HttpStatus.OK ,"Comment updated successfully!!");
        } catch (RuntimeException e){
            throw new WrongInputException("Comment isn't updated!!");
        }
    }

    public ResponseMessage deleteComment(Long commentID) {
        try {
            commentRepository.deleteById(commentID);
            return new ResponseMessage(true, HttpStatus.OK,"Comment deleted successfully!!");
        } catch (Exception e) {
            throw new ResourceNotFoundException("Comment isn't deleted!!");
        }
    }

    public List<Comment> getCategoryComment(Long roleType, Long categoryID) {
        RestTemplate restTemplate = new RestTemplate();

        SectionRoleName roleName = SectionRoleName.ROLE_PET;
        if (roleType == 1L) roleName = SectionRoleName.ROLE_CATEGORY;

        if (roleType != 1L && roleType != 2L) throw new WrongInputException("Wrong role!!");

        SectionRoleName finalRoleName = roleName;
        List<Comment> comments = commentRepository
                .findAll()
                .stream()
                .filter(c -> c.getMainRole().getName().equals(finalRoleName) && c.getCategoryID().equals(categoryID))
                .collect(Collectors.toList());

        for(Comment comment : comments){
            /*try {
                String username = restTemplate.getForObject(communicationsService.getUri("user_service")
                        + "/user/" + comment.getUsername(), String.class);
                comment.setUsername(username);
            } catch (Exception e){
                throw new ResourceNotFoundException("Can't connect to user_service!!");
            }*/
            try {
                if (roleName == SectionRoleName.ROLE_PET) {
                    Long categoryId = restTemplate.getForObject(communicationsService.getUri("pet_category_service")
                            + "/current/pet/petID/" + comment.getCategoryID(), Long.class);
                    comment.setCategoryID(categoryId);
                } else {
                    Long categoryId = restTemplate.getForObject(communicationsService.getUri("pet_category_service")
                            + "/current/rase/raseID/" + comment.getCategoryID(), Long.class);
                    comment.setCategoryID(categoryId);
                }
                commentRepository.save(comment);
            } catch (Exception e){
                //ako se ne moze spojiti na pet service
                if(e.getMessage().equals("URI is not absolute")) {//ovo je poruka koja se vraca kada nije pokrenut pet service
                    throw new ResourceNotFoundException("Can't connect to pet_category_service!!");
                }
                //u suprotnom provjerimo da li rasa ne postoji ili pet ne postoji
                if(e.getMessage().contains("pet")) {
                    throw new ResourceNotFoundException("No pet with ID " + comment.getCategoryID());
                }
                throw new ResourceNotFoundException("No rase with ID " + comment.getCategoryID());
            }
        }
        return comments;
    }

    public Comment saveComment(Comment c){
        return commentRepository.save(c);
    }

    public Optional<Comment> findById(Long id){
        return commentRepository.findById(id);
    }
}
