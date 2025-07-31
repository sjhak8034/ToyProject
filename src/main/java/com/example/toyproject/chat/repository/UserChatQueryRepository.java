package com.example.toyproject.chat.repository;

import com.example.toyproject.chat.dto.ChatDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserChatQueryRepository {
    Page<ChatDto.ChatLogResponse> findChatLogPageByRoomIdAndClassifyIsMine(Long roomId, Long userId, Pageable pageable, Long startId);
}
