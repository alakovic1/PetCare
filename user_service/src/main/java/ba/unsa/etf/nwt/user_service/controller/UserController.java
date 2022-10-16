package ba.unsa.etf.nwt.user_service.controller;

import ba.unsa.etf.nwt.user_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.user_service.exception.WrongInputException;
import ba.unsa.etf.nwt.user_service.model.User;
import ba.unsa.etf.nwt.user_service.model.roles.Role;
import ba.unsa.etf.nwt.user_service.rabbitmq.CommentServiceMessage;
import ba.unsa.etf.nwt.user_service.rabbitmq.MessagingConfig;
import ba.unsa.etf.nwt.user_service.request.UserProfileRequest;
import ba.unsa.etf.nwt.user_service.request.UserRequest;
import ba.unsa.etf.nwt.user_service.response.AvailabilityResponse;
import ba.unsa.etf.nwt.user_service.response.ResponseMessage;
import ba.unsa.etf.nwt.user_service.response.UserProfileResponse;
import ba.unsa.etf.nwt.user_service.security.CurrentUser;
import ba.unsa.etf.nwt.user_service.security.UserPrincipal;
import ba.unsa.etf.nwt.user_service.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/users/{username}")
    public UserProfileResponse getUserProfile(@PathVariable(value = "username") String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        String role = "user";
        if(hasAdminRole){
            role = "admin";
        }

        return new UserProfileResponse(user.getName(), user.getSurname(), user.getUsername(), user.getEmail(), role);
    }

    @GetMapping("/user/usernameCheck")
    public AvailabilityResponse checkUsernameAvailability(@RequestParam(value = "username") String username) {
        return new AvailabilityResponse(!userService.existsByUsername(username));
    }

    @GetMapping("/user/emailCheck")
    public AvailabilityResponse checkEmailAvailability(@RequestParam(value = "email") String email) {
        return new AvailabilityResponse(!userService.existsByEmail(email));
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/user/me")
    public UserProfileResponse getCurrentUser(@CurrentUser UserPrincipal currentUser){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        String role = "user";
        if(hasAdminRole){
            role = "admin";
        }

        return new UserProfileResponse(currentUser.getName(), currentUser.getSurname(), currentUser.getUsername(), currentUser.getEmail(), role);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PutMapping("/user/update")
    public ResponseMessage updateUserProfile(@Valid @RequestBody UserProfileRequest userProfileRequest,
                                             @CurrentUser UserPrincipal currentUser){

        //korisnici mogu updateovat samo vlastiti profil
        if(!currentUser.getEmail().equals(userProfileRequest.getEmail())){
            throw new WrongInputException("Email not the same as current users!");
        }

        User user = userService.findByEmail(userProfileRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if(!userProfileRequest.getUsername().equals(currentUser.getUsername())) {
            if (userService.existsByUsername(userProfileRequest.getUsername())) {
                throw new WrongInputException("That username is already taken!");
            }
        }

        user.setName(userProfileRequest.getName());
        user.setSurname(userProfileRequest.getSurname());
        user.setUsername(userProfileRequest.getUsername());
        userService.save(user);
        return new ResponseMessage(true, HttpStatus.OK, "Profile successfully updated.");
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping("/user/delete")
    public ResponseMessage deleteUser(@Valid @RequestBody UserRequest userRequest, @CurrentUser UserPrincipal currentUser){

        //korisnici mogu obrisati samo vlastiti account
        if(!currentUser.getEmail().equals(userRequest.getEmail())){
            throw new WrongInputException("Email not the same as current users!");
        }

        User user = userService.findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {

            //send message to comment_service
            CommentServiceMessage commentServiceMessage = new CommentServiceMessage(user.getUsername(),
                    "This is the username of a user that has been deleted!");
            rabbitTemplate.convertAndSend(MessagingConfig.USER_COMMENT_SERVICE_EXCHANGE,
                    MessagingConfig.USER_COMMENT_SERVICE_ROUTING_KEY, commentServiceMessage);

            //delete user
            userService.delete(user);

            return new ResponseMessage(true, HttpStatus.OK, "You have successfully deleted your account.");
        } else {
            throw new WrongInputException("Wrong password!");
        }
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/user/username/{id}")
    public UserProfileResponse getUsers(@PathVariable Long id) {
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

            String currentRole = "";
            for (Role role1 : user.getRoles()) {
                currentRole = role1.getName().name();
            }

            String role = "";
            if (currentRole.equals("ROLE_USER")) {
                role = "user";
            } else {
                role = "admin";
            }

            return new UserProfileResponse(user.getName(), user.getSurname(), user.getUsername(), user.getEmail(), role);
        } catch (ResourceNotFoundException e){
            return new UserProfileResponse("", "", "UNKNOWN", "", "");
        }
    }
}
