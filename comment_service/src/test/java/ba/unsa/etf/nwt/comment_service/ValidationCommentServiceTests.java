package ba.unsa.etf.nwt.comment_service;

import ba.unsa.etf.nwt.comment_service.model.Comment;
import ba.unsa.etf.nwt.comment_service.model.Reply;
import ba.unsa.etf.nwt.comment_service.model.sectionRole.MainRole;
import ba.unsa.etf.nwt.comment_service.model.sectionRole.SectionRoleName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
class ValidationCommentServiceTests {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void CreateCommentNoValues() {
        Comment comment = new Comment();
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithOnlyUserId() {
        Comment comment = new Comment();
        comment.setUsername("alakovic1");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithOnlyTitle() {
        Comment comment = new Comment();
        comment.setTitle("Create Comment With Only Title");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithOnlyContent() {
        Comment comment = new Comment();
        comment.setContent("Content 1");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithOnlyShortContent() {
        Comment comment = new Comment();
        comment.setContent("C");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdAndContent() {
        Comment comment = new Comment();
        comment.setUsername("alakovic1");
        comment.setContent("Content 1");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdAndShortContent() {
        Comment comment = new Comment();
        comment.setUsername("alakovic1");
        comment.setContent("C");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdAndTitle() {
        Comment comment = new Comment();
        comment.setUsername("alakovic1");
        comment.setTitle("Title");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdAndShortTitle() {
        Comment comment = new Comment();
        comment.setUsername("alakovic1");
        comment.setTitle("Title");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdContentAndTitle() {
        Comment comment = new Comment();
        comment.setUsername("alakovic1");
        comment.setTitle("Title");
        comment.setContent("Content 1");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdShortContentAndTitle() {
        Comment comment = new Comment();
        comment.setUsername("alakovic1");
        comment.setTitle("Title");
        comment.setContent("C");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdContentAndShortTitle() {
        Comment comment = new Comment();
        comment.setUsername("alakovic1");
        comment.setTitle("T");
        comment.setContent("Content");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdShortContentAndShortTitle() {
        Comment comment = new Comment();
        comment.setUsername("alakovic1");
        comment.setTitle("T");
        comment.setContent("C");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdContentTitleAndRole() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setUsername("alakovic1");
        comment.setTitle("Title");
        comment.setContent("Content");
        comment.setRoles(r2);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdContentTitleAndRole2() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_PET);
        comment.setUsername("alakovic1");
        comment.setTitle("Title");
        comment.setContent("Content");
        comment.setRoles(r2);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdShortContentTitleAndRole2() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_PET);
        comment.setUsername("alakovic1");
        comment.setTitle("Title");
        comment.setContent("C");
        comment.setRoles(r2);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdContentShortTitleAndRole2() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_PET);
        comment.setUsername("alakovic1");
        comment.setTitle("T");
        comment.setContent("Content");
        comment.setRoles(r2);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdShortContentShortTitleAndRole2() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_PET);
        comment.setUsername("alakovic1");
        comment.setTitle("T");
        comment.setContent("C");
        comment.setRoles(r2);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdShortContentTitleAndRole() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setUsername("alakovic1");
        comment.setTitle("Title");
        comment.setContent("C");
        comment.setRoles(r2);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdContentShortTitleAndRole() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setUsername("alakovic1");
        comment.setTitle("T");
        comment.setContent("Content");
        comment.setRoles(r2);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdShortContentShortTitleAndRole() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setUsername("alakovic1");
        comment.setTitle("T");
        comment.setContent("C");
        comment.setRoles(r2);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithUserIdContentTitleRoleAndCategoryId() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_PET);
        comment.setUsername("alakovic1");
        comment.setTitle("Title");
        comment.setContent("Content");
        comment.setRoles(r2);
        comment.setCategoryID(1L);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithRoleAndCategoryId() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_PET);
        comment.setRoles(r2);
        comment.setCategoryID(1L);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateCommentWithRole2AndCategoryId() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setRoles(r2);
        comment.setCategoryID(1L);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void TitleShort() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setUsername("alakovic1");
        comment.setTitle("Q");
        comment.setContent( "Favorite animal?");
        comment.setRoles(r2);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void TitleShortNoUserID() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setTitle("Q");
        comment.setContent( "Favorite animal?");
        comment.setRoles(r2);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void TitleShortNoRole() {
        Comment comment = new Comment();
        comment.setUsername("alakovic1");
        comment.setTitle("Q");
        comment.setContent( "Favorite animal?");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void ContentShort() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setUsername("alakovic1");
        comment.setTitle("Question 13");
        comment.setContent( "O");
        comment.setRoles(r2);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void ContentShortNoUserID() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setTitle("Question 13");
        comment.setContent( "O");
        comment.setRoles(r2);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void ContentShortNoRole() {
        Comment comment = new Comment();
        comment.setUsername("alakovic1");
        comment.setTitle("Question 13");
        comment.setContent( "O");
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void onlyRole() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setRoles(r2);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void onlyCategoryId() {
        Comment comment = new Comment();
        comment.setCategoryID(1L);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CorrectComment() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setUsername("alakovic1");
        comment.setTitle("Question 13");
        comment.setContent( "Favorite animal?");
        comment.setRoles(r2);
        comment.setCategoryID(1L);
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void NullReply() {
        Reply reply = new Reply();
        Set<ConstraintViolation<Reply>> violations = validator.validate(reply);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateReplyWithOnlyContent() {
        Reply reply = new Reply();
        reply.setContent("Reply 1");
        Set<ConstraintViolation<Reply>> violations = validator.validate(reply);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void CreateReplyWithOnlyShortContent() {
        Reply reply = new Reply();
        reply.setContent("R");
        Set<ConstraintViolation<Reply>> violations = validator.validate(reply);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateReplyWithOnlyUserId() {
        Reply reply = new Reply();
        reply.setUsername("user");
        Set<ConstraintViolation<Reply>> violations = validator.validate(reply);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CreateReplyWithContentUserId() {
        Reply reply = new Reply();
        reply.setContent("Reply 1");
        reply.setUsername("alakovic1");
        Set<ConstraintViolation<Reply>> violations = validator.validate(reply);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void CreateReplyWithShortContentUserId() {
        Reply reply = new Reply();
        reply.setContent("R");
        reply.setUsername("user");
        Set<ConstraintViolation<Reply>> violations = validator.validate(reply);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void ShortContent() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setUsername("alakovic1");
        comment.setTitle("Question 13");
        comment.setContent( "Favorite animal?");
        comment.setRoles(r2);
        Reply reply = new Reply(comment, "user", "C");
        Set<ConstraintViolation<Reply>> violations = validator.validate(reply);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void ShortContentAndNoUser() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setUsername("alakovic1");
        comment.setTitle("Question 13");
        comment.setContent( "Favorite animal?");
        comment.setRoles(r2);
        Reply reply = new Reply();
        reply.setComment(comment);
        reply.setContent("C");
        Set<ConstraintViolation<Reply>> violations = validator.validate(reply);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void contentAndNoUser() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setUsername("alakovic1");
        comment.setTitle("Question 13");
        comment.setContent( "Favorite animal?");
        comment.setRoles(r2);
        Reply reply = new Reply();
        reply.setComment(comment);
        reply.setContent("Content");
        Set<ConstraintViolation<Reply>> violations = validator.validate(reply);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void onlyComment() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setUsername("alakovic1");
        comment.setTitle("Question 13");
        comment.setContent( "Favorite animal?");
        comment.setRoles(r2);
        Reply reply = new Reply();
        reply.setComment(comment);
        Set<ConstraintViolation<Reply>> violations = validator.validate(reply);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void CorrectReply() {
        Comment comment = new Comment();
        MainRole r2 = new MainRole(SectionRoleName.ROLE_CATEGORY);
        comment.setUsername("alakovic1");
        comment.setTitle("Question 13");
        comment.setContent( "Favorite animal?");
        comment.setRoles(r2);
        Reply reply = new Reply(comment, "user", "Amazing");
        Set<ConstraintViolation<Reply>> violations = validator.validate(reply);
        assertTrue(violations.isEmpty());
    }
}
