package ba.unsa.etf.nwt.notification_service.rabbitmq.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NotificationAdoptServiceMessage {

    private Long userId;
    private Long requestId;
    private Boolean approved;
    private Boolean isAdd;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Boolean getAdd() {
        return isAdd;
    }

    public void setAdd(Boolean add) {
        isAdd = add;
    }
}
