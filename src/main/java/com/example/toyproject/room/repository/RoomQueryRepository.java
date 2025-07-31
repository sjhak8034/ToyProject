package com.example.toyproject.room.repository;

import com.example.toyproject.room.dto.RoomDto;
import org.springframework.data.domain.Page;

public interface RoomQueryRepository {
    Page<RoomDto.RoomInfoDto> findAllRoomInfo(
            Long userId, Integer page, Integer size, String sort, String roomName, String userNickName, Boolean mine);
}
