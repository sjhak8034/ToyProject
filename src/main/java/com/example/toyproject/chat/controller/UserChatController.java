package com.example.toyproject.chat.controller;

import com.example.toyproject.chat.dto.ChatDto;
import com.example.toyproject.chat.service.UserChatService;
import com.example.toyproject.room.repository.GroupRoomRepository;
import com.example.toyproject.room.service.GroupRoomService;
import com.example.toyproject.user.entity.User;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@OpenAPIDefinition(
        tags = {
                @Tag(name = "PlayerChat", description = "플레이어 채팅 관련 API")
        }
)
@RequestMapping("/user-chats")
@RestController
public class UserChatController {
    private final UserChatService userChatService;

    public UserChatController(UserChatService userChatService) {
        this.userChatService = userChatService;
    }

    @Operation(
            summary = "플레이어 채팅 기록 조회",
            description = "플레이어가 특정 방의 채팅 기록을 조회하는 API"
    )
    @GetMapping("/rooms/{roomId}/chats")
    public ResponseEntity<Page<ChatDto.ChatLogResponse>> getChatHistory(
            @PathVariable(name = "roomId") Long roomId,
            Authentication authentication,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @RequestParam(name = "sort", defaultValue = "createdAt,desc") String sort,
            @RequestParam(name = "startId", required = false) Long startId
    ) {
        User user = (User) authentication.getPrincipal();
        String[] sortParams = sort.split(",");
        Sort pageSort = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Page<ChatDto.ChatLogResponse> chatHistory = userChatService.getChatHistory(roomId, user, PageRequest.of(page,size, pageSort), startId);
        return ResponseEntity.ok(chatHistory);
    }

}
