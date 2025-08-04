package com.example.toyproject.room.controller;

import com.example.toyproject.room.dto.RoomDto;
import com.example.toyproject.room.service.GroupRoomService;
import com.example.toyproject.room.service.RoomService;
import com.example.toyproject.room.service.SingleRoomService;
import com.example.toyproject.user.entity.User;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@OpenAPIDefinition(tags = {
        @Tag(name = "GroupRoom", description = "그룹방 관련 API")
})
@RequestMapping("/rooms")
@RestController
public class GroupRoomController {

    private final RoomService roomService;
    private GroupRoomService groupRoomService;
    private SingleRoomService singleRoomService;

    public GroupRoomController(GroupRoomService groupRoomService, RoomService roomService) {
        this.groupRoomService = groupRoomService;
        this.roomService = roomService;
    }

    @Operation(
            summary = "그룹 채팅방 생성",
            description = "그룹 채팅방을 생성합니다. 이 API는 그룹 채팅방을 생성하는 기능을 제공합니다. " +
                    "성공적으로 생성된 경우, 생성된 방의 정보가 포함된 응답을 반환합니다."
    )
    @PostMapping("/group")
    public ResponseEntity<RoomDto.SaveGroupRoomResponse> createGroupRoom(
            @RequestBody RoomDto.SaveGroupRoomRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok().body(groupRoomService.saveGroupRoom(request, (User) authentication.getPrincipal()));
    }

    @Operation(
            summary = "그룹 채팅방 정보 조회",
            description = "특정 그룹 채팅방의 정보를 조회합니다. 이 API는 그룹 채팅방의 상세 정보를 반환합니다."
    )
    @GetMapping
    public ResponseEntity<Page<RoomDto.RoomInfo>> findAllRoom(
            Authentication authentication,
            @RequestParam (name = "page", defaultValue = "0") Integer page,
            @RequestParam (name = "size", defaultValue = "20") Integer size,
            @RequestParam (name = "sort", defaultValue = "createdAt,desc") String sort,
            @RequestParam (name = "roomName", required = false) String roomName,
            @RequestParam (name = "userNickName", required = false) String userNickName,
            @RequestParam (name = "mine", defaultValue = "false") Boolean mine
    ) {
        return ResponseEntity.ok().body(
                roomService.findAllRoomInfo(
                        (User) authentication.getPrincipal(),
                        page,
                        size,
                        sort,
                        roomName,
                        userNickName,
                        mine
                )
        );
    }

    @Operation(
            summary = "싱글(ai) 채팅방 생성"
            , description = "싱글 플레이어(인공지능) 채팅방을 생성합니다. 이 API는 싱글 플레이어 채팅방을 생성하는 기능을 제공합니다. " +
                    "성공적으로 생성된 경우, 생성된 방의 정보가 포함된 응답을 반환합니다."
    )
    @PostMapping("/single")
    public ResponseEntity<RoomDto.SaveSingleRoomResponse> createSingleRoom(
            @RequestBody RoomDto.SaveSingleRoomRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok().body(singleRoomService.saveSingleRoom(request, (User) authentication.getPrincipal()));
    }
}
