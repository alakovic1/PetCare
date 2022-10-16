package ba.unsa.etf.nwt.notification_service.security;

import ba.unsa.etf.nwt.notification_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.notification_service.security.user_model.User;
import ba.unsa.etf.nwt.notification_service.service.CommunicationsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final CommunicationsService communicationsService;

    public CustomUserDetailsService(CommunicationsService communicationsService) {
        this.communicationsService = communicationsService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        // access user_service database
        RestTemplate restTemplate = new RestTemplate();

        try {
            User user = restTemplate.getForObject(communicationsService.getUri("user_service")
                    + "/auth/load/usernameEmail/" + usernameOrEmail, User.class);

            return UserPrincipal.create(user);

        } catch (Exception e){
            //throw new ResourceNotFoundException("Can't connect to user_service!!");
        }
        return null;
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {

        RestTemplate restTemplate = new RestTemplate();

        try {
            User user = restTemplate.getForObject(communicationsService.getUri("user_service")
                    + "/auth/load/id/" + id, User.class);

            return UserPrincipal.create(user);

        } catch (Exception e){
            //throw new ResourceNotFoundException("Can't connect to user_service!!");
        }
        return null;
    }
}
