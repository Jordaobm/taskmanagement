package com.taskmanagement.taskmanagement.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PermissionEnum {

    ADMINISTRADOR(1, "Administrator"),
    USER(2, "User");

    private final Integer id;
    private final String description;

    PermissionEnum(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public static PermissionEnum valueOf(Integer id) {
        for (PermissionEnum value : PermissionEnum.values()) {
            if (value.id.equals(id)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid PermissionEnum code");
    }

}
