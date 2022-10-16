package ba.unsa.etf.nwt.comment_service.response;

import org.springframework.http.HttpStatus;

public class ResponseMessage {
    private Boolean success;
    private HttpStatus status;
    private String message;

    public ResponseMessage(Boolean success, HttpStatus status, String message) {
        this.success = success;
        this.status = status;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
