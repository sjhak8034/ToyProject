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

    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;

    public UserPlayer(User user, Player player) {
        this.user = user;
        this.player = player;
    }
}
