package ba.unsa.etf.nwt.user_service.controller;

import ba.unsa.etf.nwt.user_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.user_service.exception.WrongInputException;
import ba.unsa.etf.nwt.user_service.model.InvalidTokens;
import ba.unsa.etf.nwt.user_service.model.Question;
import ba.unsa.etf.nwt.user_service.model.User;
import ba.unsa.etf.nwt.user_service.model.roles.Role;
import ba.unsa.etf.nwt.user_service.model.roles.RoleName;
import ba.unsa.etf.nwt.user_service.rabbitmq.MessagingConfig;
import ba.unsa.etf.nwt.user_service.rabbitmq.NotificationServiceMessage;
import ba.unsa.etf.nwt.user_service.request.LoginRequest;
import ba.unsa.etf.nwt.user_service.request.RegistrationRequest;
import ba.unsa.etf.nwt.user_service.request.password_requests.PasswordQuestionRequest;
import ba.unsa.etf.nwt.user_service.response.JwtAuthenticationResponse;
import ba.unsa.etf.nwt.user_service.response.ResponseMessage;
import ba.unsa.etf.nwt.user_service.security.CurrentUser;
import ba.unsa.etf.nwt.user_service.security.JwtTokenProvider;
import ba.unsa.etf.nwt.user_service.security.UserPrincipal;
import ba.unsa.etf.nwt.user_service.service.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final CommunicationsService communicationsService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private InvalidTokensService invalidTokensService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider, QuestionService questionService, AnswerService answerService, CommunicationsService communicationsService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.questionService = questionService;
        this.answerService = answerService;
        this.communicationsService = communicationsService;
    }

    @PostMapping("/register/{questionId}")
    public ResponseEntity<?> registration(@PathVariable Long questionId, @Valid @RequestBody RegistrationRequest registrationRequest) {

        if(userService.existsByUsername(registrationRequest.getUsername())) {
            throw new WrongInputException("Username is already taken!");
        }

        if(userService.existsByEmail(registrationRequest.getEmail())) {
            throw new WrongInputException("Email Address already in use!");
        }

        if(registrationRequest.getAnswer()==null || registrationRequest.getAnswer().getText().isEmpty()){
            throw new WrongInputException("Answer text must not be empty!");
        }

        User user = new User(registrationRequest.getName(), registrationRequest.getSurname(),
                registrationRequest.getEmail(), registrationRequest.getUsername(),
                registrationRequest.getPassword(), registrationRequest.getAnswer());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleService.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Roles are not set!!"));

        user.setRoles(Collections.singleton(userRole));

        if (!questionService.existsById(questionId)) {
            throw new ResourceNotFoundException("Question with given id does not exist!");
        }

        Question question = questionService.findById(questionId).get();
        user.getAnswer().setQuestion(question);
        answerService.save(user.getAnswer());
        User result = userService.save(user);

        /*RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseMessage responseMessage = restTemplate.getForObject(communicationsService.getUri("notification_service")
                    + "/notifications/public/add/" + result.getId(), ResponseMessage.class);
            System.out.println(responseMessage.getMessage());
        } catch (Exception ue){
            System.out.println("Can't connect to notification_service!");
        }*/

        //send message to notification_service
        NotificationServiceMessage notificationServiceMessage = new NotificationServiceMessage(user.getId(),
                "There is a new registered user, check the list of users!");
        rabbitTemplate.convertAndSend(MessagingConfig.USER_NOTIFICATION_SERVICE_EXCHANGE,
                MessagingConfig.USER_NOTIFICATION_SERVICE_ROUTING_KEY, notificationServiceMessage);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();
        return ResponseEntity.created(location).body(new ResponseMessage(true, HttpStatus.OK, "User registered successfully"));
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);

            //find id, for frontend
            User user = userService.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail())
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with username or email : " + loginRequest.getUsernameOrEmail())
                    );

            return new JwtAuthenticationResponse(user.getId(), jwt);
            //return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));

        } catch (UsernameNotFoundException e) {
            throw new WrongInputException("User not found!");
        } catch (Exception e){
            throw new WrongInputException("Wrong username/email or password!");
        }
    }

    @PostMapping("/login/token")
    public String getJustToken(@Valid @RequestBody LoginRequest loginRequest) {
        return login(loginRequest).getAccessToken();
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/logout")
    public ResponseMessage logout(@RequestHeader("Authorization") String token,
                                  @Valid @RequestBody PasswordQuestionRequest request,
                                  @CurrentUser UserPrincipal currentUser){

        //svi korisnici se mogu odjaviti samo sa vlastitih profila
        if(!currentUser.getEmail().equals(request.getEmail())){
            throw new WrongInputException("Email not the same as current users!");
        }

        String newInvalidToken = "";

        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            newInvalidToken = token.substring(7, token.length());
        }

        InvalidTokens invalidToken = new InvalidTokens();
        invalidToken.setToken(newInvalidToken);

        invalidTokensService.addToken(invalidToken);

        return new ResponseMessage(true, HttpStatus.OK,
                "You have successfully logout from your account!");
    }

}
