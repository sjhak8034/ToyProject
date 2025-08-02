package com.example.toyproject.player.dto;

import com.example.toyproject.common.enums.PlayerType;
import com.example.toyproject.player.entity.Player;
import com.example.toyproject.player.entity.UserPlayer;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PlayerDto {

    @NoArgsConstructor
    @Getter
    public static class UserPlayerDto {
        private Player player;
        private UserPlayer userPlayer;

        @QueryProjection
        public UserPlayerDto(Player player, UserPlayer userPlayer) {
            this.player = player;
            this.userPlayer = userPlayer;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class PlayerInfo {
        private Long playerId;
        private String nickname;
        private PlayerType playerType;
        private String picture;

        @QueryProjection
        public PlayerInfo(Long playerId, String nickname, PlayerType playerType, String picture) {
            this.playerId = playerId;
            this.nickname = nickname;
            this.playerType = playerType;
            this.picture = picture;
        }
    }
}
