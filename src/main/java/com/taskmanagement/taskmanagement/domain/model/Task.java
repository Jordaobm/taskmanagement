package com.taskmanagement.taskmanagement.domain.model;

import com.taskmanagement.taskmanagement.domain.enums.StatusEnum;
import com.taskmanagement.taskmanagement.domain.exception.TaskException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Integer status;

    @ManyToOne
    private User author;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public StatusEnum getStatus() {
        try {
            return StatusEnum.valueOf(status);
        } catch (RuntimeException runtimeException) {
            throw new TaskException("Status inválido!");
        }
    }

    public void setStatus(StatusEnum status) {


        this.status = status.getId();
    }

}
