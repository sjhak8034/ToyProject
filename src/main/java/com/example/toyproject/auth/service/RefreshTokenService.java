package com.example.toyproject.auth.service;

import com.example.toyproject.auth.entity.RefreshTokenEntity;
import com.example.toyproject.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * RefreshToken이 존재하는지 확인하고, 해당 유저의 다른 RefreshToken들을 삭제합니다.
     * @param token
     * @param email
     * @return
     */
    @Transactional
    public Boolean matchReshTokenAndDeleteOtherRefreshTokens(String token, String email) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(token)
                .orElse(null);
        refreshTokenRepository.deleteAllByEmailAndTokenIsNotLike(email, token);
        if(refreshTokenEntity != null) {
            refreshTokenRepository.save(refreshTokenEntity);
            return true;
        }
        return false;
    }

    // refresh token을 만들경우 무조건 저장해야함
    @Transactional
    public void saveRefreshToken(String token, String email) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(email, token);
        // 기존에 같은 이메일로 저장된 토큰이 있다면 삭제
        refreshTokenRepository.deleteAllByEmail(email);
        refreshTokenRepository.save(refreshTokenEntity);
    }
}
