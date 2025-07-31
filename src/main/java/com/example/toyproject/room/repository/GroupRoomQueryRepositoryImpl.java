package com.example.toyproject.room.repository;

import com.example.toyproject.room.dto.QRoomDto_GroupRoomDto;
import com.example.toyproject.room.dto.RoomDto;
import com.example.toyproject.room.entity.QGroupRoom;
import com.example.toyproject.room.entity.QRoom;
import com.example.toyproject.room.entity.QSingleRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupRoomQueryRepositoryImpl implements GroupRoomQueryRepository {
    private final QRoom qRoom = QRoom.room;
    private final QGroupRoom qGroupRoom = QGroupRoom.groupRoom;
    private final QSingleRoom qSingleRoom = QSingleRoom.singleRoom;
    private final JPAQueryFactory queryFactory;

    @Override
    public RoomDto.GroupRoomDto findGroupRoomByRoomId(Long roomId) {
        return queryFactory
                .select(new QRoomDto_GroupRoomDto(qRoom, qGroupRoom))
                .from(qRoom)
                .join(qGroupRoom).on(qGroupRoom.room.id.eq(qRoom.id))  // 수정된 join 구문
                .where(qRoom.id.eq(roomId))
                .fetchOne();
    }
}
