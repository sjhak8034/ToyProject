package com.example.toyproject.room.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "group_rooms", indexes = {
        @Index(name = "idx_group_room_invite_code", columnList = "invite_code")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GroupRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invite_code", unique = true)
    private String invite_code;

    private String password;

    @OneToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;


    public GroupRoom(String invite_code, Room room, String password) {
        this.invite_code = invite_code;
        this.room = room;
        this.password = password;
    }
}
