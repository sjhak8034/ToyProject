package com.example.toyproject.player.entity;

import com.example.toyproject.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "user_players")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long playerId;

    public UserPlayer(User user, Long playerId) {
        this.user = user;
        this.playerId = playerId;
    }
}
