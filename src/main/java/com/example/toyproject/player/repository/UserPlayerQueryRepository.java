package com.example.toyproject.player.repository;

import com.example.toyproject.player.dto.PlayerDto;

public interface UserPlayerQueryRepository {
    PlayerDto.UserPlayerDto findUserPlayerById(Long userId);
}
