package com.taskmanagement.taskmanagement.api.dtos.input;

import com.taskmanagement.taskmanagement.domain.enums.StatusEnum;
import com.taskmanagement.taskmanagement.domain.exception.TaskException;
import lombok.Data;

@Data
public class TaskInputDTO {

    private Long id;
    private String title;
    private String description;
    private Integer status;

    public Integer getStatus() {
        try {
            return StatusEnum.valueOf(status).getId();
        } catch (RuntimeException runtimeException) {
            throw new TaskException("Status inválido!");
        }
    }
}
