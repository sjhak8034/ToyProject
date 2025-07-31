package com.example.toyproject.chat.service;

import com.example.toyproject.chat.dto.ChatDto;
import com.example.toyproject.chat.entity.Chat;
import com.example.toyproject.chat.entity.UserChat;
import com.example.toyproject.chat.repository.ChatRepository;
import com.example.toyproject.chat.repository.UserChatRepository;
import com.example.toyproject.common.enums.PlayerType;
import com.example.toyproject.player.dto.PlayerDto;
import com.example.toyproject.player.entity.Player;
import com.example.toyproject.player.entity.UserPlayer;
import com.example.toyproject.player.service.UserPlayerService;
import com.example.toyproject.room.dto.RoomDto;
import com.example.toyproject.room.service.GroupRoomService;
import com.example.toyproject.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserChatService {
    private final UserChatRepository userChatRepository;
    private final ChatRepository chatRepository;
    private final GroupRoomService groupRoomService;
    private final UserPlayerService userPlayerService;

    public UserChatService(UserChatRepository userChatRepository,
                           ChatRepository chatRepository,
                           GroupRoomService groupRoomService, UserPlayerService userPlayerService) {
        this.userChatRepository = userChatRepository;
        this.chatRepository = chatRepository;
        this.groupRoomService = groupRoomService;
        this.userPlayerService = userPlayerService;
    }

    /**
     * websocket을 통해 채팅 메시지를 저장하는 메서드입니다.
     * chat 저장 후 UserChat 저장
     * usMine은 null로 해서 프론트에서 처리
     * @param request
     * @param user
     * @return
     */
    @Transactional
    public ChatDto.ChatLogResponse saveChat(Long roomId, ChatDto.SaveChatRequest request, User user) {
        RoomDto.GroupRoomDto groupRoomDto = groupRoomService.findGroupRoomByRoomId(roomId);
        Chat chat = new Chat(PlayerType.USER, groupRoomDto.getRoom(), request.message());
        chatRepository.save(chat);
        PlayerDto.UserPlayerDto userPlayerDto = userPlayerService.getUserPlayerDtoByUserId(user.getId());
        UserPlayer userPlayer = userPlayerDto.getUserPlayer();
        UserChat userChat = new UserChat(
                userPlayer,
                chat
        );
        userChatRepository.save(userChat);
        return new ChatDto.ChatLogResponse(
                chat.getId(),
                user.getId(),
                chat.getMessage(),
                user.getNickname(),
                chat.getCreatedAt(),
                null
        );
    }

    public Page<ChatDto.ChatLogResponse> getChatHistory(Long roomId, User user, Pageable pageable, Long startId) {


        return userChatRepository.findChatLogPageByRoomIdAndClassifyIsMine(roomId, user.getId(), pageable, startId);
    }
}
