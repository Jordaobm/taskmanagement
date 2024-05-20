package com.taskmanagement.taskmanagement.domain.exception;

public class TaskInvalid extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public String message;

    public TaskInvalid(String message) {
        super(message);
        this.message = message;
    }

}