package com.example.toyproject.player.service;

import com.example.toyproject.ai.entity.AiRole;
import com.example.toyproject.common.enums.PlayerType;
import com.example.toyproject.player.entity.AiPlayer;
import com.example.toyproject.player.entity.Player;
import com.example.toyproject.player.repository.AiPlayerRepository;
import com.example.toyproject.player.repository.AiRoleRepository;
import com.example.toyproject.player.repository.PlayerRepository;
import com.example.toyproject.room.entity.Room;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AiPlayerService {


    private final AiRoleRepository aiRoleRepository;
    private final AiPlayerRepository aiPlayerRepository;
    private final PlayerRepository playerRepository;

    public AiPlayerService(AiRoleRepository aiRoleRepository, AiPlayerRepository aiPlayerRepository, PlayerRepository playerRepository) {
        this.aiRoleRepository = aiRoleRepository;
        this.aiPlayerRepository = aiPlayerRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public AiPlayer saveAiPlayer(Room room) {
        Player player = new Player(
                PlayerType.AI,
                room
        );
        playerRepository.save(player);
        AiRole aiRole = new AiRole(
                "test" // AI role name
        );
        aiRoleRepository.save(aiRole);
        AiPlayer aiPlayer = new AiPlayer(
                player,
                "test",
                "https://chat-project-jhs.s3.ap-northeast-2.amazonaws.com/images/character1.png",
                aiRole
        );
        aiPlayerRepository.save(aiPlayer);

        return aiPlayer;
    }
}
