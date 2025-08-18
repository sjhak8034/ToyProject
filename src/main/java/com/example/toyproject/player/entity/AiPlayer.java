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

    private String name;

    private String picture;

    @JoinColumn(nullable = false, name = "role_id")
    @ManyToOne()
    private AiRole aiRole;

    public AiPlayer(Player player, String name, String picture, AiRole aiRole) {
        this.player = player;
        this.name = name;
        this.picture = picture;
        this.aiRole = aiRole;
    }
}
