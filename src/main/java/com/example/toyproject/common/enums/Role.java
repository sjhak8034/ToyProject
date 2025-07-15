package com.example.toyproject.common.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.getValue().equals(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + role);
    }
}
