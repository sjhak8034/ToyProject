package com.example.toyproject.room.dto;

import com.example.toyproject.room.entity.GroupRoom;
import com.example.toyproject.room.entity.Room;
import com.example.toyproject.room.entity.SingleRoom;
import com.example.toyproject.room.enums.RoomStatus;
import com.example.toyproject.room.enums.RoomType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RoomDto {

    public record SaveGroupRoomRequest(
        String roomName,
        String password,
        Integer maxPlayers
    ) {}

    public record SaveSingleRoomRequest(
        String roomName
    ) {}

    public record SaveGroupRoomResponse(
        Long roomId,
        String roomName,
        String roomType,
        String password,
        String inviteCode
    ) {}

    public record SaveSingleRoomResponse(
        Long roomId
    ) {}


    @Getter
    @NoArgsConstructor
    public static class RoomInfo {
        private Long roomId;
        private String roomName;
        private RoomType roomType;
        private String ownerNickname;
        private Long ownerId;
        private Integer currentPlayers;
        private Integer maxPlayers;
        private RoomStatus roomStatus;
        private Boolean isPrivate;
        private Boolean isParticipated;
        private Boolean isMine;

        @QueryProjection
        public RoomInfo(Long roomId, String roomName, RoomType roomType, String ownerNickname, Long ownerId, Integer maxPlayers, Integer currentPlayers, RoomStatus roomStatus, Boolean isPrivate, Boolean isParticipated, Boolean isMine) {
            this.roomId = roomId;
            this.roomName = roomName;
            this.roomType = roomType;
            this.ownerNickname = ownerNickname;
            this.ownerId = ownerId;
            this.maxPlayers = maxPlayers;
            this.currentPlayers = currentPlayers;
            this.roomStatus = roomStatus;
            this.isPrivate = isPrivate;
            this.isParticipated = isParticipated;
            this.isMine = isMine;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class GroupRoomDto {
        private Room room;
        private GroupRoom groupRoom;

        @QueryProjection
        public GroupRoomDto(Room room, GroupRoom groupRoom) {
            this.room = room;
            this.groupRoom = groupRoom;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SingleRoomDto {
        private Room room;
        private SingleRoom singleRoom;
        @QueryProjection
        public SingleRoomDto(Room room, SingleRoom singleRoom) {
            this.room = room;
            this.singleRoom = singleRoom;
        }
    }
}
