package com.taskmanagement.taskmanagement.domain.exception;

public class UserAlreadyExist extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public String message;

    public UserAlreadyExist(String message) {
        super(message);
        this.message = message;
    }

}