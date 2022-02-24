package br.com.api.meetingroom.handler;

import br.com.api.meetingroom.exception.ConflictException;
import br.com.api.meetingroom.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handlerNotFoundException(Exception exception) {
        return createResponseEntity(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handlerConflictException(Exception exception) {
        return createResponseEntity(HttpStatus.CONFLICT, exception);
    }

    private ResponseEntity<Object> createResponseEntity(HttpStatus httpStatus, Exception exception) {
        return new ResponseEntity<>(
                ResponseErro.newBuilder()
                        .status(httpStatus.getReasonPhrase())
                        .code(httpStatus.value())
                        .message(exception.getMessage())
                        .build(),
                httpStatus
        );
    }
}
