package ba.unsa.etf.nwt.user_service.exception;

import ba.unsa.etf.nwt.user_service.response.ErrorResponse;
import ba.unsa.etf.nwt.user_service.response.ResponseMessage;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDenied(HttpServletRequest request, AccessDeniedException ex) {

        List<String> details = new ArrayList<>();

        if(request.getUserPrincipal() != null){
            details.add("User " + request.getUserPrincipal().getName() + " with this role is not authorized to access this route!");

            ErrorResponse er = new ErrorResponse(new ResponseMessage(false, HttpStatus.FORBIDDEN, "Unauthorized request!"), details);
            return new ResponseEntity<>(er, HttpStatus.FORBIDDEN);
        }
        else {
            details.add("This route is authorized and requires login!");

            ErrorResponse er = new ErrorResponse(new ResponseMessage(false, HttpStatus.UNAUTHORIZED, "Unauthorized request!"), details);
            return new ResponseEntity<>(er, HttpStatus.UNAUTHORIZED);
        }
    }

}
