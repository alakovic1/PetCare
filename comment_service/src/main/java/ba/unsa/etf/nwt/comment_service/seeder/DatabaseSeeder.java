package ba.unsa.etf.nwt.comment_service.seeder;

import ba.unsa.etf.nwt.comment_service.exception.WrongInputException;
import ba.unsa.etf.nwt.comment_service.model.Comment;
import ba.unsa.etf.nwt.comment_service.model.Reply;
import ba.unsa.etf.nwt.comment_service.model.sectionRole.MainRole;
import ba.unsa.etf.nwt.comment_service.model.sectionRole.SectionRoleName;
import ba.unsa.etf.nwt.comment_service.response.ResponseMessage;
import ba.unsa.etf.nwt.comment_service.service.CommentService;
import ba.unsa.etf.nwt.comment_service.service.MainRoleService;
import ba.unsa.etf.nwt.comment_service.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DatabaseSeeder {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private MainRoleService mainRoleService;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedDatabase();
    }

    private void seedDatabase() {
        MainRole r1 = createRole(SectionRoleName.ROLE_CATEGORY);
        MainRole r2 = createRole(SectionRoleName.ROLE_PET);

        Comment c = createComment("username", 1L,"Question", "What kind of dog do you want?", r1);
        Comment c1 = createComment("username", 1L,"How is this?", "Great!", r1);
        Comment c2 = createComment("username", 1L,"Amazing", "No other words", r2);
        Comment c3 = createComment("username", 1L,"Wow", "Lorem ipsum", r2);
        Reply rp1 = createReply("alakovic1", c, "Sweet and relaxed, friendly towards everyone.");
        Reply rp2 = createReply("epita1", c1, "Fantastic!!!!!!!!!!");
        Reply rp3 = createReply("smujcinovi1", c3, "Friendly...");

    }

    private MainRole createRole(SectionRoleName sectionRoleName) {
        MainRole r = new MainRole(sectionRoleName);
        mainRoleService.addRole(r);
        return r;
    }

    private Comment createComment(String username, Long CategoryID,String title, String content, MainRole role) {
        Comment comment = new Comment();
        comment.setUsername(username);
        comment.setCategoryID(CategoryID);
        comment.setTitle(title);
        comment.setContent(content);
        comment.setRoles(role);
        commentService.saveComment(comment);
        return comment;
    }

    private Reply createReply(String username, Comment comment, String content) {
        Reply reply = new Reply();
        reply.setUsername(username);
        reply.setComment(comment);
        reply.setContent(content);
        replyService.saveReply(reply);
        return reply;
    }

}
