package com.example.toyproject.chat.repository;

import com.example.toyproject.chat.entity.Chat;
import com.example.toyproject.chat.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
