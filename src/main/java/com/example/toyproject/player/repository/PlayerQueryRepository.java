package com.example.toyproject.player.repository;

import com.example.toyproject.player.dto.PlayerDto;

import java.util.List;

public interface PlayerQueryRepository {
    List<PlayerDto.PlayerInfo> findPlayerInfoByRoomId(Long roomId);
}
