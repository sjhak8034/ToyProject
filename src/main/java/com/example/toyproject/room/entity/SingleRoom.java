package com.example.toyproject.room.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "single_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SingleRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double expectedCost;

    @OneToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public SingleRoom(Double expectedCost, Room room) {
        this.expectedCost = expectedCost;
        this.room = room;
    }
}
