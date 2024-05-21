package com.taskmanagement.taskmanagement.domain.exception;

public class UserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public String message;

    public UserException(String message) {
        super(message);
        this.message = message;
    }

}