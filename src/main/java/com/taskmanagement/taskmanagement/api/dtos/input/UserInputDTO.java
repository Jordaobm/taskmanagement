package com.taskmanagement.taskmanagement.api.dtos.input;

import lombok.Data;

@Data
public class UserInputDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Integer role;
}
