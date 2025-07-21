package com.example.toyproject.user.service;

import com.example.toyproject.user.dto.UserDto;
import com.example.toyproject.user.entity.User;
import com.example.toyproject.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public UserDto.UserProfile getUserProfile(User user) {
        return new UserDto.UserProfile(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getPicture(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }



}
