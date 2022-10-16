package ba.unsa.etf.nwt.notification_service.controller;

import ba.unsa.etf.nwt.notification_service.model.Notification;
import ba.unsa.etf.nwt.notification_service.response.ResponseMessage;
import ba.unsa.etf.nwt.notification_service.security.CurrentUser;
import ba.unsa.etf.nwt.notification_service.security.UserPrincipal;
import ba.unsa.etf.nwt.notification_service.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@AllArgsConstructor
@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    //jedna ruta za obje role, samo drugacije funkcije
    //vrati sve neprocitane notifikacije
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/notifications/all/unread/{userID}")
    public List<Notification> getAllUnreadNotifications(@PathVariable(value = "userID") Long userID, @CurrentUser UserPrincipal currentUser){

        //pronalazak role trenutnog korisnika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        //admini
        if(hasAdminRole){
            return notificationService.getAllUnreadAdminsNotifications(currentUser);
        }
        //user
        else {
            return notificationService.getAllUnreadUsersNotifications(userID, currentUser);
        }
    }

    //jedna ruta za obje role, samo drugacije funkcije
    //vrati sve notifikacije u zavisnosti od role
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/notifications/all/{userID}")
    public List<Notification> getAllNotifications(@PathVariable(value = "userID") Long userID, @CurrentUser UserPrincipal currentUser){

        //pronalazak role trenutnog korisnika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        //admini
        if(hasAdminRole){
            return notificationService.getAllAdminsNotifications(currentUser);
        }
        //user
        else {
            return notificationService.getAllUsersNotifications(userID, currentUser);
        }
    }

    //jedna ruta za obje role, samo drugacije funkcije
    //postavi na read notifikacije
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PutMapping("/notifications/setRead/{userID}")
    public ResponseMessage setNotificationsOnRead(@PathVariable(value = "userID") Long userID, @CurrentUser UserPrincipal currentUser){

        //pronalazak role trenutnog korisnika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        //admini
        if(hasAdminRole){
            return notificationService.setNotificationsOnReadAdmin(currentUser);
        }
        //user
        else {
            return notificationService.setNotificationsOnReadUser(userID, currentUser);
        }
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping("/notifications/delete/{userID}/{id}")
    public ResponseMessage deleteNotification(@PathVariable(value = "userID") Long userID, @PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currentUser){

        //pronalazak role trenutnog korisnika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        //admini
        if(hasAdminRole){
            return notificationService.deleteAdminNotification(id, currentUser);
        }
        //user
        else {
            return notificationService.deleteUserNotification(userID, id, currentUser);
        }
    }

}
