package com.example.toyproject.room.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Table(name = "single_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SingleRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal expectedCost;

    @OneToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public SingleRoom(BigDecimal expectedCost, Room room) {
        this.expectedCost = expectedCost;
        this.room = room;
    }
}
