package com.example.toyproject.room.entity;

import com.example.toyproject.common.entity.BaseTimeEntity;
import com.example.toyproject.room.enums.RoomStatus;
import com.example.toyproject.room.enums.RoomType;
import com.example.toyproject.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Table(name = "rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Room extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean is_private;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;

    @Column(nullable = false)
    private Integer maxPlayers;

    private LocalDateTime deletedAt;

    public Room(boolean is_private, String name, RoomType type, User owner, RoomStatus status, Integer maxPlayers) {
        this.is_private = is_private;
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.status = status;
        this.maxPlayers = maxPlayers;
    }
}
