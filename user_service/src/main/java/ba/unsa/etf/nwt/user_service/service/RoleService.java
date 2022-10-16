package ba.unsa.etf.nwt.user_service.service;

import ba.unsa.etf.nwt.user_service.model.roles.Role;
import ba.unsa.etf.nwt.user_service.model.roles.RoleName;
import ba.unsa.etf.nwt.user_service.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role save(Role role){
        return roleRepository.save(role);
    }

    public Optional<Role> findByName(RoleName roleName){
       return roleRepository.findByName(roleName);
    }
}
