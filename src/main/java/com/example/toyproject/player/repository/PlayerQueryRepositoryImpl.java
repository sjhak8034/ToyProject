package com.example.toyproject.player.repository;

import com.example.toyproject.player.dto.PlayerDto;
import com.example.toyproject.player.dto.QPlayerDto_PlayerInfo;
import com.example.toyproject.player.entity.QAiPlayer;
import com.example.toyproject.player.entity.QPlayer;
import com.example.toyproject.player.entity.QUserPlayer;
import com.example.toyproject.user.entity.QUser;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PlayerQueryRepositoryImpl implements PlayerQueryRepository {

    private final QPlayer qPlayer = QPlayer.player;
    private final QUser qUser = QUser.user;
    private final QUserPlayer qUserPlayer = QUserPlayer.userPlayer;
    private final QAiPlayer qAiPlayer = QAiPlayer.aiPlayer;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlayerDto.PlayerInfo> findPlayerInfoByRoomId(Long roomId) {
        return queryFactory
                .select(new QPlayerDto_PlayerInfo(
                        qPlayer.id,
                        new CaseBuilder()
                                .when(qUser.id.isNotNull()).then(qUser.nickname)
                                .otherwise(qAiPlayer.name),
                        qPlayer.type,
                        new CaseBuilder()
                                .when(qUser.id.isNotNull()).then(qUser.picture)
                                .otherwise(qAiPlayer.picture)
                ))
                .from(qPlayer)
                .leftJoin(qUserPlayer).on(qUserPlayer.player.id.eq(qPlayer.id))
                .leftJoin(qUser).on(qUser.id.eq(qUserPlayer.user.id))
                .leftJoin(qAiPlayer).on(qAiPlayer.player.id.eq(qPlayer.id))
                .where(qPlayer.room.id.eq(roomId))
                .fetch();

    }


}
