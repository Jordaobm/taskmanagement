package com.taskmanagement.taskmanagement.api.dtos.input;

import com.taskmanagement.taskmanagement.domain.model.User;
import lombok.Data;

@Data
public class TaskInputDTO {

    private Long id;
    private String title;
    private String description;
    private Integer status;
}
