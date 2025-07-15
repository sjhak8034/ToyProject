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

    @Column(nullable = false)
    private Long playerId;

    @JoinColumn(nullable = false, name = "ai_role_id")
    @ManyToOne()
    private AiRole aiRole;

    AiPlayer(Long playerId, AiRole aiRole) {
        this.playerId = playerId;
        this.aiRole = aiRole;
    }
}
