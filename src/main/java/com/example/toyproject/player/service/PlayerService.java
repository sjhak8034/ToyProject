package com.example.toyproject.player.service;

import com.example.toyproject.player.dto.PlayerDto;
import com.example.toyproject.player.repository.PlayerRepository;
import com.example.toyproject.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<PlayerDto.PlayerInfo> getPlayersInRoom(User user, Long roomId) {
        return playerRepository.findPlayerInfoByRoomId(roomId);
    }
}
