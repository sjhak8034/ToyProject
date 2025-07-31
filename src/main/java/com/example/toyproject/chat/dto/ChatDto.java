package com.example.toyproject.chat.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

public class ChatDto {

    public record SaveChatRequest(
        String message
    ) {}

    @Getter
    public static class ChatLogResponse{
        private final Long id;
        private final Long userId;
        private final String message;
        private final String senderNickname;
        private final LocalDateTime createdAt;
        private final Boolean isMine;
        @QueryProjection
        public ChatLogResponse(Long id, Long userId, String message, String senderNickname, LocalDateTime createdAt, Boolean isMine) {
            this.id = id;
            this.userId = userId;
            this.message = message;
            this.senderNickname = senderNickname;
            this.createdAt = createdAt;
            this.isMine = isMine;
        }
    }
}
