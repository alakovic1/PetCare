package ba.unsa.etf.nwt.user_service.controller;

import ba.unsa.etf.nwt.user_service.model.Question;
import ba.unsa.etf.nwt.user_service.response.ResponseMessage;
import ba.unsa.etf.nwt.user_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/questions")
    public List<Question> getQuestions() {
        return questionService.findAll();
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/questions")
    public ResponseMessage createQuestion(@Valid @RequestBody Question question) {
        questionService.save(question);
        return new ResponseMessage(true, HttpStatus.OK,"Question added successfully.");
    }
}
