package com.taskmanagement.taskmanagement.domain.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {

    PENDING(1, "Pending"),
    COMPLETED(2, "Completed"),
    CANCELED(3, "Canceled");

    private final Integer id;
    private final String description;

    StatusEnum(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public static StatusEnum valueOf(Integer id) {
        for (StatusEnum value : StatusEnum.values()) {
            if (value.id.equals(id)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus code");
    }

}
