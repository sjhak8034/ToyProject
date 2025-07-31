package com.example.toyproject.player.service;

import com.example.toyproject.common.enums.PlayerType;
import com.example.toyproject.common.reentrantLock.ReentrantLockSetting;
import com.example.toyproject.player.dto.PlayerDto;
import com.example.toyproject.player.entity.Player;
import com.example.toyproject.player.entity.UserPlayer;
import com.example.toyproject.player.repository.PlayerRepository;
import com.example.toyproject.player.repository.UserPlayerRepository;
import com.example.toyproject.room.entity.Room;
import com.example.toyproject.room.service.RoomService;
import com.example.toyproject.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserPlayerService {
    private final UserPlayerRepository userPlayerRepository;
    private final RoomService roomService;
    private final PlayerRepository playerRepository;

    public UserPlayerService(UserPlayerRepository userPlayerRepository, RoomService roomService, PlayerRepository playerRepository) {
        this.userPlayerRepository = userPlayerRepository;
        this.roomService = roomService;
        this.playerRepository = playerRepository;
    }

    public PlayerDto.UserPlayerDto getUserPlayerDtoByUserId(Long userId) {
        PlayerDto.UserPlayerDto userPlayerDto = userPlayerRepository.findUserPlayerById(userId);
        if (userPlayerDto == null) {
            throw new IllegalArgumentException("UserPlayer not found with id: " + userId);
        }
        return userPlayerDto;
    }

    // 현재 방에 참여중인 경우는 제외
    // player 저장 후 UserPlayer 저장
    // playerId를 반환

    @ReentrantLockSetting(key = "'userId:' + #user.id + 'roomId:' + #roomId", name = "saveUserPlayer")
    @Transactional
    public Long saveUserPlayer(User user, Long roomId) {
        Room room = roomService.findRoomById(roomId);
        UserPlayer existingUserPlayer = userPlayerRepository.findByUserAndPlayer_Room(user, room);
        if (existingUserPlayer != null) {
            log.info("UserPlayer already exists for user: {} in room: {}", user.getId(), roomId);
            return existingUserPlayer.getId();
        }
        Player player = playerRepository.save(new Player(PlayerType.USER, room));
        UserPlayer userPlayer = new UserPlayer(user, player);
        userPlayerRepository.save(userPlayer);
        return player.getId();
    }

    public Boolean checkUserPlayerExists(User user, Long roomId) {
        Room room = roomService.findRoomById(roomId);
        UserPlayer existingUserPlayer = userPlayerRepository.findByUserAndPlayer_Room(user, room);
        if (existingUserPlayer != null) {
            return true;
        }
        return false;
    }

}
