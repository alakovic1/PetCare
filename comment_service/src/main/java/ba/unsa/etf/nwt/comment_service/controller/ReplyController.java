package ba.unsa.etf.nwt.comment_service.controller;

import ba.unsa.etf.nwt.comment_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.comment_service.exception.WrongInputException;
import ba.unsa.etf.nwt.comment_service.model.Comment;
import ba.unsa.etf.nwt.comment_service.model.Reply;
import ba.unsa.etf.nwt.comment_service.response.ResponseMessage;
import ba.unsa.etf.nwt.comment_service.security.CurrentUser;
import ba.unsa.etf.nwt.comment_service.security.UserPrincipal;
import ba.unsa.etf.nwt.comment_service.service.ReplyService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping("/reply")
    public List<Reply> getReplies() {
        return replyService.getReply();
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/reply/{commentId}")
    public ResponseMessage addReply(@Valid @RequestBody Reply reply, @PathVariable Long commentId, @CurrentUser UserPrincipal currentUser) {
        return replyService.addReply(reply, commentId, currentUser);
    }

    @GetMapping("/reply/comment/{commentID}")
    public List<Reply> getRepliesForComment(@PathVariable Long commentID){
        return replyService.getAllReplyForComment(commentID);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PutMapping("/reply/{replyID}")
    public ResponseMessage updateReply(@Valid @RequestBody Reply reply, @PathVariable Long replyID, @CurrentUser UserPrincipal currentUser) {

        Reply newReply = replyService.findById(replyID)
                .orElseThrow(() -> new ResourceNotFoundException("Reply not found!"));

        //korisnici mogu updateovati samo vlastiti odgovor
        if(!currentUser.getUsername().equals(newReply.getUsername())){
            throw new WrongInputException("This reply doesn't belong to current user!");
        }

        return replyService.updateReply(reply, replyID);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping("/reply/{replyID}")
    public ResponseMessage deleteReply(@PathVariable Long replyID, @CurrentUser UserPrincipal currentUser){

        //pronalazak role trenutnog korisnika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        //svi admini i samo useri ciji je odgovor imaju pravo brisanja istog
        if(hasAdminRole){
            return replyService.deleteReply(replyID);
        }

        Reply reply = replyService.findById(replyID)
                .orElseThrow(() -> new ResourceNotFoundException("Reply not found!"));

        //korisnici mogu obrisati samo vlastiti odgovor
        if(!currentUser.getUsername().equals(reply.getUsername())){
            throw new WrongInputException("Current user is not admin and this reply doesn't belong to current user!");
        }

        return replyService.deleteReply(replyID);
    }
}
