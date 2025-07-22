package com.example.toyproject.user.controller;

import com.example.toyproject.user.dto.UserDto;
import com.example.toyproject.user.entity.User;
import com.example.toyproject.user.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "사용자 API",
                version = "1.0",
                description = "사용자 관련 API"
        )
)
@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 사용자 프로필 조회
     * @param authentication 인증 정보
     * @return 사용자 프로필 정보
     */
    @Operation(
            summary = "사용자 프로필 조회",
            description = "인증된 사용자의 프로필 정보를 조회합니다."
    )
    @GetMapping("/profile")
    public ResponseEntity<UserDto.UserProfile> userProfile(
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok().body(userService.getUserProfile(user));
    }


}
