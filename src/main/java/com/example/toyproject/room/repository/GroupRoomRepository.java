package com.example.toyproject.room.repository;

import com.example.toyproject.room.entity.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRoomRepository extends JpaRepository<GroupRoom, Long>, GroupRoomQueryRepository  {

    // Additional query methods specific to GroupRoom can be defined here if needed
}
