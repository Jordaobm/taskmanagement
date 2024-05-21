package com.taskmanagement.taskmanagement.api.exception;

import com.taskmanagement.taskmanagement.domain.exception.AuthenticationException;
import com.taskmanagement.taskmanagement.domain.exception.TaskInvalidException;
import com.taskmanagement.taskmanagement.domain.exception.UserAlreadyExistException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<StandardError> userAlreadyExist(UserAlreadyExistException userAlreadyExistException,
                                                          HttpServletRequest httpServletRequest) {

        StandardError standardError = new StandardError();
        standardError.setError(userAlreadyExistException.getMessage());
        standardError.setMessage(userAlreadyExistException.getMessage());
        standardError.setStatus(HttpStatus.BAD_REQUEST.value());
        standardError.setPath(httpServletRequest.getRequestURI());
        standardError.setTimestamp(Instant.now());

        return ResponseEntity.status(standardError.getStatus()).body(standardError);

    }


    @ExceptionHandler(TaskInvalidException.class)
    public ResponseEntity<StandardError> taskInvalid(TaskInvalidException taskInvalidException,
                                                     HttpServletRequest httpServletRequest) {

        StandardError standardError = new StandardError();
        standardError.setError(taskInvalidException.getMessage());
        standardError.setMessage(taskInvalidException.getMessage());
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
