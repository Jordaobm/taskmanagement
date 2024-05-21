package com.taskmanagement.taskmanagement.domain.exception;

public class UserAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public String message;

    public UserAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }

}