package com.taskmanagement.taskmanagement.domain.exception;

public class TaskInvalidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public String message;

    public TaskInvalidException(String message) {
        super(message);
        this.message = message;
    }

}