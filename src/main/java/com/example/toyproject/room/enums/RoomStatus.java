package com.example.toyproject.room.enums;

import lombok.Getter;

@Getter
public enum RoomStatus {
    OPEN("OPEN"),
    CLOSED("CLOSED");

    private final String name;

    RoomStatus(String name) {
        this.name = name;
    }
}
