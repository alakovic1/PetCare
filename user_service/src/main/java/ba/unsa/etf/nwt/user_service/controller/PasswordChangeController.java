package ba.unsa.etf.nwt.user_service.controller;

import ba.unsa.etf.nwt.user_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.user_service.exception.WrongInputException;
import ba.unsa.etf.nwt.user_service.model.User;
import ba.unsa.etf.nwt.user_service.request.password_requests.PasswordAnswerRequest;
import ba.unsa.etf.nwt.user_service.request.password_requests.PasswordChangeRequest;
import ba.unsa.etf.nwt.user_service.request.password_requests.PasswordQuestionRequest;
import ba.unsa.etf.nwt.user_service.response.QuestionResponse;
import ba.unsa.etf.nwt.user_service.response.ResponseMessage;
import ba.unsa.etf.nwt.user_service.security.CurrentUser;
import ba.unsa.etf.nwt.user_service.security.UserPrincipal;
import ba.unsa.etf.nwt.user_service.service.PasswordService;
import ba.unsa.etf.nwt.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("/change")
public class PasswordChangeController {
    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/securityquestion")
    public QuestionResponse getSecurityQuestion(@Valid @RequestBody PasswordQuestionRequest passwordQuestionRequest,
                                                @CurrentUser UserPrincipal currentUser){

        //korisnici mogu preuzeti samo vlastito pitanje
        if(!currentUser.getEmail().equals(passwordQuestionRequest.getEmail())){
            throw new WrongInputException("Email not the same as current users!");
        }

        return passwordService.getQuestion(passwordQuestionRequest);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/answerQuestion")
    public ResponseMessage answerQuestion(@Valid @RequestBody PasswordAnswerRequest passwordAnswerRequest,
                                          @CurrentUser UserPrincipal currentUser) {

        //korisnici mogu odgovoriti samo na vlastito pitanje
        if(!currentUser.getEmail().equals(passwordAnswerRequest.getEmail())){
            throw new WrongInputException("Email not the same as current users!");
        }

        return passwordService.getAnswerOfQuestion(passwordAnswerRequest);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/newPassword")
    public ResponseMessage getNewPassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest,
                                          @CurrentUser UserPrincipal currentUser) {

        //korisnici mogu promijeniti samo vlastitu sifru
        if(!currentUser.getEmail().equals(passwordChangeRequest.getEmail())){
            throw new WrongInputException("Email not the same as current users!");
        }

        User user = userService.findByEmail(passwordChangeRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (passwordChangeRequest.getAnswer().getText().equals(user.getAnswer().getText())) {

            if (!passwordEncoder.matches(passwordChangeRequest.getOldPassword(), user.getPassword())) {
                throw new WrongInputException("Old password is not a match!");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            currentUser.getUsername(),
                            passwordChangeRequest.getOldPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
            userService.save(user);
            return new ResponseMessage(true, HttpStatus.OK, "You have successfully changed your password.");
        } else {
            throw new WrongInputException("Wrong answer!");
        }
    }
}
