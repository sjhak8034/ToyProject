package com.example.toyproject.room.service;

import com.example.toyproject.room.dto.RoomDto;
import com.example.toyproject.room.entity.Room;
import com.example.toyproject.room.repository.RoomRepository;
import com.example.toyproject.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Page<RoomDto.RoomInfoDto> findAllRoomInfo(User user, Integer page, Integer size, String sort, String roomName, String userNickName, Boolean mine) {
        return roomRepository.findAllRoomInfo(user.getId(), page, size, sort, roomName, userNickName, mine);
    }

    public Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
    }
}
