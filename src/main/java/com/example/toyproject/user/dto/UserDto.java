package com.example.toyproject.user.dto;

import java.time.LocalDateTime;

public class UserDto {

    public record UserProfile(
        Long id,
        String username,
        String nickname,
        String email,
        String picture,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {}

}
