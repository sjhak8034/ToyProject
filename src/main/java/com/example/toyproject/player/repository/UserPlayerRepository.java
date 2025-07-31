package com.example.toyproject.player.repository;

import com.example.toyproject.player.entity.UserPlayer;
import com.example.toyproject.room.entity.Room;
import com.example.toyproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPlayerRepository extends JpaRepository<UserPlayer, Long>, UserPlayerQueryRepository {

    UserPlayer findByUserAndPlayer_Room(User user, Room room);
}
