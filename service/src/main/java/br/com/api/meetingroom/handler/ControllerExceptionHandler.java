package br.com.api.meetingroom.handler;

import br.com.api.meetingroom.exception.BusinessException;
import br.com.api.meetingroom.exception.ConflictException;
import br.com.api.meetingroom.exception.InvalidRequestException;
import br.com.api.meetingroom.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handlerNotFoundException(Exception exception) {
        return createResponseEntity(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(
            {
                    ConflictException.class,
                    BusinessException.class
            }
    )
    public ResponseEntity<Object> handlerConflictException(Exception exception) {
        return createResponseEntity(HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> handlerInvalidRequestException(Exception exception) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerMethodArgumentNotValidException(MethodArgumentNotValidException argumentNotValidException) {
        return argumentNotValidException
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(
                        Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
                );

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
