package com.taskmanagement.taskmanagement.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taskmanagement.taskmanagement.domain.enums.PermissionEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private Integer role;

    public PermissionEnum getRole() {
        return PermissionEnum.valueOf(role);
    }

    public void setRole(PermissionEnum role) {
        this.role = role.getId();
    }
}
