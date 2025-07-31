package com.example.toyproject.chat.entity;

import com.example.toyproject.common.entity.BaseTimeEntity;
import com.example.toyproject.common.enums.PlayerType;
import com.example.toyproject.player.entity.Player;
import com.example.toyproject.room.entity.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "chats")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerType type;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private String message;

    public Chat(PlayerType type, Room room,  String message) {
        this.type = type;
        this.room = room;
        this.message = message;
    }

}
