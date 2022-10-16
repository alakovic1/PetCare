package ba.unsa.etf.nwt.adopt_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongInputException extends RuntimeException {
    public WrongInputException(String message) {
        super(message);
    }

    public WrongInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
