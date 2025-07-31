package com.example.toyproject.player.dto;

import com.example.toyproject.player.entity.Player;
import com.example.toyproject.player.entity.UserPlayer;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PlayerDto {

    @NoArgsConstructor
    @Getter
    public static class UserPlayerDto{
        private Player player;
        private UserPlayer userPlayer;

        @QueryProjection
        public UserPlayerDto(Player player, UserPlayer userPlayer) {
            this.player = player;
            this.userPlayer = userPlayer;
        }
    }
}
