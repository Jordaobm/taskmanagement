package com.taskmanagement.taskmanagement.domain.exception;

public class TaskException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public String message;

    public TaskException(String message) {
        super(message);
        this.message = message;
    }

}