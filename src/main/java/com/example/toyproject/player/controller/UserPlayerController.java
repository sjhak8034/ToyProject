package com.example.toyproject.player.controller;

import com.example.toyproject.player.dto.PlayerDto;
import com.example.toyproject.player.service.PlayerService;
import com.example.toyproject.player.service.UserPlayerService;
import com.example.toyproject.user.entity.User;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@OpenAPIDefinition(tags = {
        @io.swagger.v3.oas.annotations.tags.Tag(name = "UserPlayer", description = "User Player Management")
})
@RequestMapping("/players")
@RestController
public class UserPlayerController {
    private final UserPlayerService userPlayerService;
    private final PlayerService playerService;

    public UserPlayerController(UserPlayerService userPlayerService, PlayerService playerService) {
        this.userPlayerService = userPlayerService;
        this.playerService = playerService;
    }

    @Operation(
            summary = "user player를 생성",
            description = "방에 들어갈때 검증 후 user player를 생성하는 API"
    )
    @PostMapping("/users/rooms/{roomId}")
    public ResponseEntity<Long> createUserPlayer(
            @PathVariable Long roomId,
            Authentication authentication
    ) {
        return ResponseEntity.ok().body(userPlayerService.saveUserPlayer(
                (User) authentication.getPrincipal(),
                roomId
        ));

    }

    @Operation(
            summary = "user player가 존재하는지 확인",
            description = "user player가 존재하는지 확인하는 API"
    )
    @GetMapping("/users/exists/rooms/{roomId}")
    public ResponseEntity<Boolean> checkUserPlayerExists(
            @PathVariable Long roomId,
            Authentication authentication
    ) {
        return ResponseEntity.ok().body(userPlayerService.checkUserPlayerExists(
                (User) authentication.getPrincipal(),
                roomId
        ));
    }
    @Operation(
            summary = "참여한 인원들의 player 조회(ai 포함)",
            description = "특정 방에 참여한 인원들의 player를 조회하는 API (AI 포함)"
    )
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<List<PlayerDto.PlayerInfo>> getPlayersInRoom(
            @PathVariable Long roomId,
            Authentication authentication
    ) {
        return ResponseEntity.ok().body(playerService.getPlayersInRoom(
                (User) authentication.getPrincipal(),
                roomId
        ));
    }
}