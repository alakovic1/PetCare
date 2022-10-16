package ba.unsa.etf.nwt.comment_service.controller;

import ba.unsa.etf.nwt.comment_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.comment_service.exception.WrongInputException;
import ba.unsa.etf.nwt.comment_service.model.Comment;
import ba.unsa.etf.nwt.comment_service.response.ResponseMessage;
import ba.unsa.etf.nwt.comment_service.security.CurrentUser;
import ba.unsa.etf.nwt.comment_service.security.UserPrincipal;
import ba.unsa.etf.nwt.comment_service.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@AllArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comment")
    public List<Comment> getComments() {
        return commentService.getComment();
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/comment/{mainRoleId}")
    public ResponseMessage addComment(@Valid @RequestBody Comment comment, @PathVariable Long mainRoleId, @CurrentUser UserPrincipal currentUser) {
        return commentService.addComment(comment, mainRoleId, currentUser);
    }

    @GetMapping("/comment/{commentID}")
    public Comment getOneComment(@PathVariable Long commentID){
        return commentService.getOneComment(commentID);
    }

    @GetMapping("/comment/user/{username}")
    public List<Comment> getUserComments(@PathVariable String username){
        return commentService.getUserComments(username);
    }

    //pregled svih komentara za jednog korisnika
    @GetMapping("/comment/category/{roleType}/{categoryID}")
    public List<Comment> getCategoryComment(@PathVariable Long roleType, @PathVariable Long categoryID){
        return commentService.getCategoryComment(roleType, categoryID);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PutMapping("/comment/{commentID}")
    public ResponseMessage updateComment(@Valid @RequestBody Comment comment, @PathVariable Long commentID, @CurrentUser UserPrincipal currentUser) {

        Comment newComment = commentService.findById(commentID)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found!"));

        //korisnici mogu updateovat samo vlastite komentare
        if(!currentUser.getUsername().equals(newComment.getUsername())){
            throw new WrongInputException("This comment doesn't belong to current user!");
        }

        return commentService.updateComment(comment, commentID);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping("/comment/{commentID}")
    public ResponseMessage deleteComment(@PathVariable Long commentID, @CurrentUser UserPrincipal currentUser){

        //pronalazak role trenutnog korisnika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        //svi admini i samo useri ciji je komentar imaju pravo brisanja istog
        if(hasAdminRole){
            return commentService.deleteComment(commentID);
        }

        Comment comment = commentService.findById(commentID)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found!"));

        //korisnici mogu obrisati samo vlastiti komentar
        if (!currentUser.getUsername().equals(comment.getUsername())) {
            throw new WrongInputException("Current user is not admin and this comment doesn't belong to current user!");
        }

        return commentService.deleteComment(commentID);
    }
}

