package com.example.toyproject.security.handler;

import com.example.toyproject.security.util.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

        String accessToken = tokenProvider.createAccessTokenFromOauth2(authentication);
        String refreshToken = tokenProvider.createRefreshTokenFromOauth2(authentication);

//        // JWT를 httponly 쿠키에 저장 (공백 추가)
//        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
//        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
//
//        accessTokenCookie.setHttpOnly(true);
//        // cookie.setSecure(true); // HTTPS 환경에서만 활성화
//        accessTokenCookie.setPath("/");
//        accessTokenCookie.setMaxAge(3600); // 1시간
//        accessTokenCookie.setSecure(true); // HTTPS 환경에서만 활성화
//
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(604800); // 7일
//        refreshTokenCookie.setSecure(true); // HTTPS 환경에서만 활성화


        ResponseCookie accessTokenCookie = ResponseCookie.from("s_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(3600)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(60L * 60 * 24 * 7)
                .build();


        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        // 프론트엔드 리다이렉트 URL
        getRedirectStrategy().sendRedirect(request, response, frontendUrl);
    }
}