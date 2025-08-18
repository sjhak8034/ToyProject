package com.example.toyproject.player.repository;

import com.example.toyproject.player.entity.AiPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiPlayerRepository extends JpaRepository<AiPlayer, Long> {
}
