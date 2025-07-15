package com.example.toyproject.player.entity;

import com.example.toyproject.common.enums.PlayerType;
import com.example.toyproject.room.entity.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "players")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlayerType type;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Player(PlayerType type, Room room) {
        this.type = type;
        this.room = room;
    }
}
