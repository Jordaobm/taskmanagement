package com.taskmanagement.taskmanagement.api.exception;

import com.taskmanagement.taskmanagement.domain.exception.AuthenticationException;
import com.taskmanagement.taskmanagement.domain.exception.TaskException;
import com.taskmanagement.taskmanagement.domain.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(UserException.class)
    public ResponseEntity<StandardError> userAlreadyExist(UserException userException,
                                                          HttpServletRequest httpServletRequest) {

        StandardError standardError = new StandardError();
        standardError.setError(userException.getMessage());
        standardError.setMessage(userException.getMessage());
        standardError.setStatus(HttpStatus.BAD_REQUEST.value());
        standardError.setPath(httpServletRequest.getRequestURI());
        standardError.setTimestamp(Instant.now());

        return ResponseEntity.status(standardError.getStatus()).body(standardError);

    }


    @ExceptionHandler(TaskException.class)
    public ResponseEntity<StandardError> taskInvalid(TaskException taskException,
                                                     HttpServletRequest httpServletRequest) {

        StandardError standardError = new StandardError();
        standardError.setError(taskException.getMessage());
        standardError.setMessage(taskException.getMessage());
        standardError.setStatus(HttpStatus.BAD_REQUEST.value());
        standardError.setPath(httpServletRequest.getRequestURI());
        standardError.setTimestamp(Instant.now());

        return ResponseEntity.status(standardError.getStatus()).body(standardError);

    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<StandardError> authentication(AuthenticationException authenticationException,
                                                        HttpServletRequest httpServletRequest) {

        StandardError standardError = new StandardError();
        standardError.setError(authenticationException.getMessage());
        standardError.setMessage(authenticationException.getMessage());
        standardError.setStatus(HttpStatus.UNAUTHORIZED.value());
        standardError.setPath(httpServletRequest.getRequestURI());
        standardError.setTimestamp(Instant.now());

        return ResponseEntity.status(standardError.getStatus()).body(standardError);

    }

}
