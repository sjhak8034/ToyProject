package com.example.toyproject.chat.controller;

import com.example.toyproject.chat.dto.ChatDto;
import com.example.toyproject.chat.service.UserChatService;
import com.example.toyproject.user.entity.User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

/**
 * WebSocket을 통해 채팅 메시지를 처리하는 컨트롤러입니다.
 * 클라이언트가 특정 방에 메시지를 보낼 때 이 컨트롤러가 호출되어 메시지를 처리하고 브로드캐스트합니다.
 */
@Controller
public class ChatSocketController {

    private final UserChatService userChatService;

    public ChatSocketController(UserChatService userChatService) {
        this.userChatService = userChatService;
    }

    @MessageMapping("/rooms/{roomId}")  // 클라이언트가 app/rooms/{roomId}로 메시지를 보낼 때 이 메서드가 호출됨 (최상단에 설정한 request mapping을 무시함)
    // @MessageMapping은 WebSocket 메시지를 처리하는 엔드포인트를 정의합니다
    // @SendTo는 메시지를 브로드캐스트할 대상 경로를 지정합니다
    // 이 경우, /topic/rooms/{roomId}로 브로드캐스트됩니다
    @SendTo("/topic/rooms/{roomId}")  // 메시지를 /topic/rooms/{roomId}로 브로드캐스트
    public ChatDto.ChatLogResponse greeting(
            @DestinationVariable Long roomId,
            Authentication authentication,
            ChatDto.SaveChatRequest message) {
        System.out.println("받은 메시지: " + message.message());

        return userChatService.saveChat(
                roomId,
                message,
                (User) authentication.getPrincipal()
        );
    }
}
