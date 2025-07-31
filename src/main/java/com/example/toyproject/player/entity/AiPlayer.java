package com.example.toyproject.player.entity;

import com.example.toyproject.ai.entity.AiRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ai_players")
public class AiPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @JoinColumn(nullable = false, name = "ai_role_id")
    @ManyToOne()
    private AiRole aiRole;

    AiPlayer(Player player, AiRole aiRole) {
        this.player = player;
        this.aiRole = aiRole;
    }
}
