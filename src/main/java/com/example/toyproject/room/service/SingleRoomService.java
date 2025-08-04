package com.example.toyproject.room.service;

import com.example.toyproject.player.service.PlayerService;
import com.example.toyproject.player.service.UserPlayerService;
import com.example.toyproject.room.dto.RoomDto;
import com.example.toyproject.room.entity.Room;
import com.example.toyproject.room.entity.SingleRoom;
import com.example.toyproject.room.enums.RoomStatus;
import com.example.toyproject.room.enums.RoomType;
import com.example.toyproject.room.repository.RoomRepository;
import com.example.toyproject.room.repository.SingleRoomRepository;
import com.example.toyproject.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SingleRoomService {
    private final RoomRepository roomRepository;
    private final SingleRoomRepository singleRoomRepository;
    private final PlayerService playerService;
    private final UserPlayerService userPlayerService;

    public SingleRoomService(RoomRepository roomRepository, SingleRoomRepository singleRoomRepository, PlayerService playerService, UserPlayerService userPlayerService) {
        this.roomRepository = roomRepository;
        this.singleRoomRepository = singleRoomRepository;
        this.playerService = playerService;
        this.userPlayerService = userPlayerService;
    }

    /**
     * Single Room 생성
     * 1. room 생성
     * 2. single room 생성
     * 3. player 생성
     * 4. ai player 생성 일단 1대1 채팅으로 생성 추후 확장 가능
     * @param request
     * @param user
     * @return
     */
    public RoomDto.SaveSingleRoomResponse saveSingleRoom(RoomDto.SaveSingleRoomRequest request, User user) {

        Room room = new Room(
                true,
                request.roomName(),
                RoomType.SINGLE,
                user,
                RoomStatus.OPEN,
                2
        );

        SingleRoom singleRoom = new SingleRoom(
                BigDecimal.ZERO,
                room
        );

        roomRepository.save(room);
        singleRoomRepository.save(singleRoom);
        userPlayerService.saveUserPlayer(user, room.getId());


        return null;
    }
}
