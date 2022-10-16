package ba.unsa.etf.nwt.user_service.security;

import ba.unsa.etf.nwt.user_service.model.User;
import ba.unsa.etf.nwt.user_service.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        try {
            // Let people login with either username or email
            User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
                    );

            return UserPrincipal.create(user);
        } catch (UsernameNotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(
                    () -> new UsernameNotFoundException("User not found with id : " + id)
            );

            return UserPrincipal.create(user);
        } catch (UsernameNotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
