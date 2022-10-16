package ba.unsa.etf.nwt.user_service.controller;

import java.util.List;

import ba.unsa.etf.nwt.user_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.user_service.model.User;
import ba.unsa.etf.nwt.user_service.response.EurekaResponse;
import ba.unsa.etf.nwt.user_service.response.LoadUserDetailsResponse;
import ba.unsa.etf.nwt.user_service.service.CommunicationsService;
import ba.unsa.etf.nwt.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommunicationsController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommunicationsService communicationsService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/eureka/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }

    @GetMapping("/eureka/service-info/{applicationName}")
    public EurekaResponse serviceInfoByApplicationName(@PathVariable String applicationName) {
        List<ServiceInstance> instances = this.discoveryClient.getInstances(applicationName);
        EurekaResponse eurekaResponse = new EurekaResponse();
        for (ServiceInstance instance : instances) {
            eurekaResponse.setServiceId(instance.getServiceId());
            eurekaResponse.setIpAddress(instance.getHost());
            eurekaResponse.setPort(instance.getPort());
        }
        return eurekaResponse;
    }

    @GetMapping("/eureka/uri/{applicationName}")
    public String getURIfromService(@PathVariable String applicationName) {
        return communicationsService.getUri(applicationName);
    }

    //rute za ostale servise

    /*@GetMapping("/user/me/username")
    public String getCurrentUsersUsername(@CurrentUser UserPrincipal currentUser){
        return currentUser.getUsername();
    }*/

    @GetMapping("/user/{username}")
    public String getUsersUsernameByUsername(@PathVariable(value = "username") String username) {
        try {
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
            return user.getUsername();
        }
        catch(ResourceNotFoundException e){
            return "UNKNOWN";
        }
    }

    /*@GetMapping("/user/me/id")
    public Long getCurrentUsersId(@CurrentUser UserPrincipal currentUser){
        return currentUser.getId();
    }*/

    @GetMapping("/auth/load/usernameEmail/{usernameOrEmail}")
    public LoadUserDetailsResponse loadUserByUsernameOrEmail(@PathVariable(value = "usernameOrEmail") String usernameOrEmail){
        try {
            User user = userService.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("User not found with username or email : " + usernameOrEmail)
                    );

            return new LoadUserDetailsResponse(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), user.getPassword(), user.getRoles());
        } catch (ResourceNotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/auth/load/id/{id}")
    public LoadUserDetailsResponse loadUserById(@PathVariable(value = "id") Long id){
        try {
            User user = userService.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("User not found with id : " + id)
            );

            return new LoadUserDetailsResponse(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), user.getPassword(), user.getRoles());
        } catch (ResourceNotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/auth/load/invalid/token/{secret}/{token}")
    public Boolean isInvalidToken(@PathVariable String secret, @PathVariable String token){
        return communicationsService.isValidToken(secret, token);
    }

}
