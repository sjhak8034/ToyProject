package com.example.toyproject.room.repository;

import com.example.toyproject.player.entity.QPlayer;
import com.example.toyproject.player.entity.QUserPlayer;
import com.example.toyproject.room.dto.QRoomDto_RoomInfo;
import com.example.toyproject.room.dto.RoomDto;
import com.example.toyproject.room.entity.QGroupRoom;
import com.example.toyproject.room.entity.QRoom;
import com.example.toyproject.room.entity.QSingleRoom;
import com.example.toyproject.user.entity.QUser;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class RoomQueryRepositoryImpl implements RoomQueryRepository {

    private final QRoom qRoom = QRoom.room;
    private final QGroupRoom qGroupRoom = QGroupRoom.groupRoom;
    private final QSingleRoom qSingleRoom = QSingleRoom.singleRoom;
    private final QUser qUser = QUser.user;
    private final QPlayer qPlayer = QPlayer.player;
    private final QUserPlayer qUserPlayer = QUserPlayer.userPlayer;
    private final JPAQueryFactory queryFactory;

    // mine true일 경우 현재 참여중인 방만 조회
    // mine false일 경우 모든 방 조회
    @Override
    public Page<RoomDto.RoomInfo> findAllRoomInfo(
            Long userId, Integer page, Integer size, String sort, String roomName, String userNickName, Boolean mine) {
        Long totalCount = 0L;
        JPAQuery<RoomDto.RoomInfo> query = queryFactory
                .select(new QRoomDto_RoomInfo(
                        qRoom.id,
                        qRoom.name,
                        qRoom.type,
                        qRoom.owner.nickname,
                        qRoom.owner.id,
                        qRoom.maxPlayers,
                        qUserPlayer.count().castToNum(Integer.class),
                        qRoom.status,
                        qRoom.is_private,
                        // 현재 유저가 참여중인 방인지 여부
                        // room에 참여중인 userPlayer와 userId가 일치하는 경우 1, 그렇지 않으면 0
                        // 이를 sum하여 0보다 큰 경우 현재 유저가 참여중인 방으로 판단
                        new CaseBuilder()
                                .when(qUserPlayer.user.id.eq(userId)).then(1L)
                                .otherwise(0L).sum().gt(0),
                        qRoom.owner.id.eq(userId)
                ));
        //

        BooleanBuilder whereClause = new BooleanBuilder();
        if (mine == false) {
            whereClause = new BooleanBuilder()
                    .and(roomName != null? qRoom.name.contains(roomName) : null)
                    .and(userNickName != null ? qUser.nickname.contains(userNickName) : null)
                    .and(qRoom.is_private.eq(false));

            query.from(qRoom)
                    .leftJoin(qPlayer).on(qPlayer.room.eq(qRoom))
                    .leftJoin(qUserPlayer).on(qUserPlayer.player.eq(qPlayer))
                    .leftJoin(qUser).on(qUserPlayer.user.eq(qUser))
                    .where(whereClause);

            totalCount = queryFactory
                    .select(qRoom.count())
                    .from(qRoom)
                    .leftJoin(qPlayer).on(qPlayer.room.eq(qRoom))
                    .leftJoin(qUserPlayer).on(qUserPlayer.player.eq(qPlayer))
                    .leftJoin(qUser).on(qUserPlayer.user.eq(qUser))
                    .where(whereClause)
                    .fetchOne();
        } else {
            query.from(qUser)
                    .leftJoin(qUserPlayer).on(qUserPlayer.user.eq(qUser))
                    .leftJoin(qPlayer).on(qUserPlayer.player.eq(qPlayer))
                    .leftJoin(qRoom).on(qPlayer.room.eq(qRoom))
                    .where(qUser.id.eq(userId));
            totalCount = queryFactory
                    .select(qRoom.count())
                    .from(qUser)
                    .leftJoin(qUserPlayer).on(qUserPlayer.user.eq(qUser))
                    .leftJoin(qPlayer).on(qUserPlayer.player.eq(qPlayer))
                    .leftJoin(qRoom).on(qPlayer.room.eq(qRoom))
                    .where(qUser.id.eq(userId))
                    .fetchOne();
        }
        query.groupBy(qRoom.id, qRoom.name, qRoom.type, qRoom.owner.nickname, qRoom.owner.id,
                qRoom.maxPlayers, qRoom.status, qRoom.is_private, qRoom.createdAt);
        for (String order : sort.split(",")) {
            String[] orderParts = order.split(":");
            String property = orderParts[0];
            boolean ascending = orderParts.length < 2 || "asc".equalsIgnoreCase(orderParts[1]);
            if ("createdAt".equals(property)) {
                if (ascending) {
                    query.orderBy(qRoom.createdAt.asc());
                } else {
                    query.orderBy(qRoom.createdAt.desc());
                }
            } else if ("name".equals(property)) {
                if (ascending) {
                    query.orderBy(qRoom.name.asc());
                } else {
                    query.orderBy(qRoom.name.desc());
                }
            } else if ("maxPlayers".equals(property)) {
                if (ascending) {
                    query.orderBy(qRoom.maxPlayers.asc());
                } else {
                    query.orderBy(qRoom.maxPlayers.desc());
                }
            } else if ("status".equals(property)) {
                if (ascending) {
                    query.orderBy(qRoom.status.asc());
                } else {
                    query.orderBy(qRoom.status.desc());
                }
            }
        }
        query.offset((long) page * size)
                .limit(size);
        if (totalCount == null) {
            totalCount = 0L;
        }
        return new PageImpl<>(query.fetch(), Pageable.ofSize(size).withPage(page), totalCount);

    }

}
