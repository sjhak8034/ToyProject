package com.example.toyproject.chat.repository;

import com.example.toyproject.chat.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRepository extends JpaRepository<UserChat, Long>, UserChatQueryRepository {


}
