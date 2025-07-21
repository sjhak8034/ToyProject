package com.example.toyproject.security.handler;

import com.example.toyproject.security.util.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // OAuth2 인증 성공 시 호출되는 메서드 현재 accesstoken만 사용
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String token = tokenProvider.createToken(authentication);

        // JWT를 httponly 쿠키에 저장 (공백 추가)
        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true); // HTTPS 환경에서만 활성화
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1시간
        System.out.println("OAuth2AuthenticationSuccessHandler: JWT 토큰 생성 완료: " + token);
        response.addCookie(cookie);
        System.out.println("OAuth2AuthenticationSuccessHandler: JWT 토큰을 쿠키에 추가했습니다.: ");
        // 프론트엔드 리다이렉트 URL
        getRedirectStrategy().sendRedirect(request, response, frontendUrl);
    }
}