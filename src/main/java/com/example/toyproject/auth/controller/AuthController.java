package com.example.toyproject.auth.controller;

import com.example.toyproject.security.util.JwtTokenProvider;
import com.example.toyproject.user.entity.User;
import com.example.toyproject.user.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthController {



    // access_token을 쿠키에 저장하는 로직은 OAuth2AuthenticationSuccessHandler에서 처리
    // logout은 spring security에서 제공하는 logout 기능을 사용 (쿠키 삭제 방식)




}