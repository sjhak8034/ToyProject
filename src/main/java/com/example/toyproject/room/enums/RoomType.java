package com.example.toyproject.room.enums;

import lombok.Getter;

@Getter
public enum RoomType {
    SINGLE("SINGLE", "AI 채팅방"),
    GROUP("GROUP", "그룹 채팅방");

    private final String name;
    private final String description;
    RoomType(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
