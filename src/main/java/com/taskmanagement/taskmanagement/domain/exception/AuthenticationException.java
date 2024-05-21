package com.taskmanagement.taskmanagement.domain.exception;

public class AuthenticationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public String message;

    public AuthenticationException(String message) {
        super(message);
        this.message = message;
    }
}
