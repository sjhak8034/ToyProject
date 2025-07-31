package com.example.toyproject.player.repository;

import com.example.toyproject.player.dto.PlayerDto;
import com.example.toyproject.player.dto.QPlayerDto_UserPlayerDto;
import com.example.toyproject.player.entity.QPlayer;
import com.example.toyproject.player.entity.QUserPlayer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserPlayerQueryRepositoryImpl implements UserPlayerQueryRepository {
    private final QUserPlayer qUserPlayer = QUserPlayer.userPlayer;
    private final QPlayer qPlayer = QPlayer.player;
    private final JPAQueryFactory queryFactory;

    @Override
    public PlayerDto.UserPlayerDto findUserPlayerById(Long userId) {
        return queryFactory
                .select(new QPlayerDto_UserPlayerDto(qPlayer, qUserPlayer))
                .from(qUserPlayer)
                .join(qUserPlayer.player, qPlayer)
                .where(qUserPlayer.user.id.eq(userId))
                .fetchOne();
    }

}
