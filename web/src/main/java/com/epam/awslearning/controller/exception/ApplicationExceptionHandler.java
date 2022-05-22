package com.epam.awslearning.controller.exception;

import com.epam.awslearning.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String DEFAULT_ERROR_MESSAGE = "Something went wrong, please contact developer";

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException e) {
        return buildErrorResponseEntity(BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleDefault(Throwable e) {
        return buildErrorResponseEntity(INTERNAL_SERVER_ERROR, DEFAULT_ERROR_MESSAGE);
    }


    private ResponseEntity<Object> buildErrorResponseEntity(HttpStatus status, String errorMessage) {
        final Map<String, Object> body = new HashMap<>();
        body.put(ERROR_MESSAGE, errorMessage);

        return new ResponseEntity<>(body, status);
    }
}
