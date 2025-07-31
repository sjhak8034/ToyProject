package com.example.toyproject.room.repository;

import com.example.toyproject.room.dto.RoomDto;

public interface GroupRoomQueryRepository {
    RoomDto.GroupRoomDto findGroupRoomByRoomId(Long roomId);

    // This class can be used to implement custom queries for the Room entity.
    // For example, you can add methods to find rooms by specific criteria,
    // or to perform complex queries that are not supported by the default JPA repository methods.

    // Example method signature:
    // public List<Room> findRoomsByCriteria(Criteria criteria) {
    //     // Implementation goes here
    // } {
}
