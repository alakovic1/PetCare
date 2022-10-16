package ba.unsa.etf.nwt.user_service.controller;

import ba.unsa.etf.nwt.user_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.user_service.model.Answer;
import ba.unsa.etf.nwt.user_service.response.ResponseMessage;
import ba.unsa.etf.nwt.user_service.service.AnswerService;
import ba.unsa.etf.nwt.user_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/questions/{questionId}/answers")
    public List<Answer> getAnswersByQuestionId(@PathVariable Long questionId) {
        return answerService.find(questionId);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/questions/{questionId}/answer")
    public ResponseMessage createAnswer(@PathVariable Long questionId, @Valid @RequestBody Answer answer) {
        questionService.findById(questionId).map(question -> {
            answer.setQuestion(question);
            answerService.save(answer);
            return new ResponseMessage(true, HttpStatus.OK, "Answer added successfully!!");
        }).orElseThrow(() -> new ResourceNotFoundException("There is no question with that ID!!"));

        return new ResponseMessage(true, HttpStatus.OK, "Answer added successfully!!");
    }
}
