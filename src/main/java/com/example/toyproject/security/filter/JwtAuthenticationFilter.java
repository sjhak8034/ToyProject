package com.example.toyproject.security.filter;


import com.example.toyproject.security.util.JwtTokenProvider;

import com.example.toyproject.user.entity.User;
import com.example.toyproject.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService; // 추가


    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolveAccessToken(request);
        String refreshToken = resolveRefreshToken(request);
        try {
            // access_token이 존재하고 유효한지 검증
            if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateAccessToken(accessToken)) {
                String email = jwtTokenProvider.getEmailFromAccessToken(accessToken);

                // 실제 User 엔티티 조회
                User user = userService.findUserByEmail(email);

                if (user != null) {
                    List<SimpleGrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromToken(accessToken);

                    // UserDetails 대신 실제 사용자 정보를 Principal로 설정
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                else {
                    throw new ServletException("Invalid access token: User not found");
                }
            }
            else {
                // 토큰이 유효하지 않거나 비어있는 경우 refresh_token을 확인 후 access_token을 갱신
                if (StringUtils.hasText(refreshToken) && jwtTokenProvider.validateRefreshToken(refreshToken)) {
                    String email = jwtTokenProvider.getEmailFromRefreshToken(refreshToken);
                    if(!jwtTokenProvider.matchRefreshToken(refreshToken, email)){
                        throw new ServletException("Refresh token does not match");
                    }
                    // refresh_token이 유효한 경우, 해당 이메일로 사용자 정보를 조회
                    // 실제 User 엔티티 조회
                    User user = userService.findUserByEmail(email);

                    if (user != null) {
                        // 사용자 권한을 DB에서 가져오기


                        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole().getValue()));


                        // UserDetails 대신 실제 사용자 정보를 Principal로 설정
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(user, null, simpleGrantedAuthorities);

                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // 새 access_token 생성
                        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);

                        // 새로운 access_token을 쿠키에 저장
                        Cookie accessCookie = new Cookie("access_token", newAccessToken);
                        accessCookie.setHttpOnly(true);
                        accessCookie.setPath("/");
                        accessCookie.setMaxAge(3600); // 1시간
                        response.addCookie(accessCookie);

                        log.info("액세스 토큰 재발급 완료: {}", email);
                    }
                    else {
                        throw new ServletException("Invalid refresh token: User not found");
                    }

                }
            }
        }catch (Exception e) {
            // 토큰이 만료되었거나 비정상적인 경우
            log.error("JWT 인증 오류: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            deleteCookies(response);
        }

        filterChain.doFilter(request, response);
    }

    // 쿠키에 저장된 JWT access 토큰을 추출하는 메소드
    private String resolveAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Stream.of(cookies)
                    .filter(cookie -> "access_token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    // 쿠키에 저장된 JWT refresh 토큰을 추출하는 메소드
    private String resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Stream.of(cookies)
                    .filter(cookie -> "refresh_token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private void deleteCookies(HttpServletResponse response) {
        // access_token 삭제
        Cookie accessCookie = new Cookie("access_token", null);
        accessCookie.setMaxAge(0);
        accessCookie.setPath("/");
        accessCookie.setHttpOnly(true);
        response.addCookie(accessCookie);

        // refresh_token 삭제 (있는 경우)
        Cookie refreshCookie = new Cookie("refresh_token", null);
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        response.addCookie(refreshCookie);
    }
}