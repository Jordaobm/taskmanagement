package com.taskmanagement.taskmanagement.api.exception;

import com.taskmanagement.taskmanagement.domain.exception.UserAlreadyExist;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.swing.text.html.parser.Entity;
import java.time.Instant;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(UserAlreadyExist.class)
    public ResponseEntity<StandardError> userAlreadyExist(UserAlreadyExist userAlreadyExist,
                                                          HttpServletRequest httpServletRequest) {

        StandardError standardError = new StandardError();
        standardError.setError(userAlreadyExist.getMessage());
        standardError.setMessage(userAlreadyExist.getMessage());
        standardError.setStatus(HttpStatus.BAD_REQUEST.value());
        standardError.setPath(httpServletRequest.getRequestURI());
        standardError.setTimestamp(Instant.now());

        return ResponseEntity.status(standardError.getStatus()).body(standardError);

    }

}
