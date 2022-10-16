package ba.unsa.etf.nwt.notification_service.repository;

import ba.unsa.etf.nwt.notification_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserID(Long userID);
    Notification findByIdAndUserID(Long notificationID, Long userID);
    List<Notification> findAllByUserIDAndRead(Long userID, Boolean read);
    //za usera
    List<Notification> findAllByUserIDAndReadAndIsForAdmin(Long userID, Boolean read, Boolean isForAdmin); //i za admina...
    List<Notification> findAllByUserIDAndIsForAdminOrderByCreatedAtDesc(Long userID, Boolean isForAdmin);
    //za admina
    List<Notification> findAllByReadAndIsForAdmin(Boolean read, Boolean isForAdmin);
    List<Notification> findAllByIsForAdmin(Boolean isForAdmin);
    List<Notification> findAllByUserIDAndIsForAdmin(Long userID, Boolean isForAdmin);
}
