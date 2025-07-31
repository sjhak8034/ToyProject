package com.example.toyproject.room.repository;

import com.example.toyproject.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomQueryRepository{
}
