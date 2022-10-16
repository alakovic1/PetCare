package ba.unsa.etf.nwt.user_service.service;

import ba.unsa.etf.nwt.user_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.user_service.exception.WrongInputException;
import ba.unsa.etf.nwt.user_service.model.User;
import ba.unsa.etf.nwt.user_service.request.password_requests.PasswordAnswerRequest;
import ba.unsa.etf.nwt.user_service.request.password_requests.PasswordQuestionRequest;
import ba.unsa.etf.nwt.user_service.response.QuestionResponse;
import ba.unsa.etf.nwt.user_service.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    @Autowired
    private UserService userService;

    public QuestionResponse getQuestion(PasswordQuestionRequest passwordQuestionRequest){
        User user = userService.findByEmail(passwordQuestionRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        return new QuestionResponse(new ResponseMessage(true, HttpStatus.OK, "Valid email, question found."), user.getAnswer().getQuestion());
    }

    public ResponseMessage getAnswerOfQuestion(PasswordAnswerRequest passwordAnswerRequest){
        User user = userService.findByEmail(passwordAnswerRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (passwordAnswerRequest.getAnswer().getText().equals(user.getAnswer().getText())) {
            return new ResponseMessage(true, HttpStatus.OK, "You have successfully answered the question.");
        } else {
            throw new WrongInputException("Wrong answer!");
        }
    }
}
