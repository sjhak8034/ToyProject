package com.example.toyproject.chat.entity;

import com.example.toyproject.player.entity.UserPlayer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "user_chats")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_player_id")
    private UserPlayer userPlayer;

    @OneToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public UserChat(UserPlayer userPlayer, Chat chat) {
        this.userPlayer = userPlayer;
        this.chat = chat;
    }
}
