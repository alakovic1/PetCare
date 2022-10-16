package ba.unsa.etf.nwt.adopt_service.response;

import java.util.List;

public class ErrorResponse {
    private ResponseMessage responseMessage;
    private List<String> details;

    public ErrorResponse(ResponseMessage responseMessage, List<String> details) {
        this.responseMessage = responseMessage;
        this.details = details;
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
