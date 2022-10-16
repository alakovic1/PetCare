package ba.unsa.etf.nwt.adopt_service.exception;

import ba.unsa.etf.nwt.adopt_service.response.ErrorResponse;
import ba.unsa.etf.nwt.adopt_service.response.ResponseMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    public ResponseEntity<Object> tempHandler(String exceptionMessage, String message, HttpStatus status) {
        List<String> details = new ArrayList<>();
        details.add(exceptionMessage);
        ErrorResponse er = new ErrorResponse(new ResponseMessage(false, status, message), details);
        return new ResponseEntity<>(er, status);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(ResourceNotFoundException e) {
        return tempHandler(e.getMessage(), "Exception for NOT_FOUND was thrown", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {WrongInputException.class})
    public ResponseEntity<Object> handleWrongInputException(WrongInputException e) {
        return tempHandler(e.getMessage(), "Exception for wrong input was thrown", HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ErrorResponse error = new ErrorResponse(new ResponseMessage(false, HttpStatus.BAD_REQUEST, "Validation Failed"), details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}