package com.example.toyproject.auth.repository;


import com.example.toyproject.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    void deleteAllByToken(String token);

    void deleteAllByEmail(String email);

    void deleteAllByEmailAndTokenIsNotLike(String email, String token);
}
