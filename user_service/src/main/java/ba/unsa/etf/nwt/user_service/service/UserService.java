package ba.unsa.etf.nwt.user_service.service;

import ba.unsa.etf.nwt.user_service.model.User;
import ba.unsa.etf.nwt.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public Boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByUsernameOrEmail(String username, String email){
        return userRepository.findByUsernameOrEmail(username, email);
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void delete(User user){
        userRepository.delete(user);
    }

    public Optional<User> findBuUsernameOrEmail(String username, String email){
        return userRepository.findByUsernameOrEmail(username, email);
    }
}
