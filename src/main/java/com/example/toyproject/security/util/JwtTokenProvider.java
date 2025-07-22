package com.example.toyproject.security.util;

import com.example.toyproject.auth.service.RefreshTokenService;
import com.example.toyproject.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key accessKey;
    private final Key refreshKey;
    private final long tokenValidityInMilliseconds;
    private final RefreshTokenService refreshTokenService;

    public JwtTokenProvider(
            @Value("${jwt.access-secret}") String accessSecret,
            @Value("${jwt.refresh-secret}") String refreshSecret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            RefreshTokenService refreshTokenService ) {
        this.accessKey = Keys.hmacShaKeyFor(accessSecret.getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes());
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.refreshTokenService = refreshTokenService;
    }

    public String createAccessTokenFromOauth2(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        List<SimpleGrantedAuthority> authorities =  authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        // jwt에 포함된 정보는 email
        return Jwts.builder()
                .setSubject(oAuth2User.getAttribute("email"))
                .claim("authorities", authorities)
                .signWith(accessKey, SignatureAlgorithm.HS512)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .compact();
    }

    public String createRefreshTokenFromOauth2(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds * 24 * 7); // Refresh token의 유효기간은 7일

        String refreshToken = Jwts.builder()
                .setSubject(oAuth2User.getAttribute("email"))
                .signWith(refreshKey, SignatureAlgorithm.HS512)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .compact();

        System.out.println("Refresh Token: " + refreshToken);
        // db에 refresh token 저장 이후 매칭하는데 사용
        refreshTokenService.saveRefreshToken(refreshToken, oAuth2User.getAttribute("email"));

        return refreshToken;
    }

    public String createAccessToken(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        List<SimpleGrantedAuthority> authorities =  authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        // jwt에 포함된 정보는 email과 권한
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("authorities", authorities)
                .signWith(accessKey, SignatureAlgorithm.HS512)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .compact();
    }

    public String getEmailFromAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getEmailFromRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, accessKey);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshKey);
    }

    public boolean matchRefreshToken(String refreshToken, String email) {
        return refreshTokenService.matchReshTokenAndDeleteOtherRefreshTokens(refreshToken, email);
    }

    private boolean validateToken(String token, Key accessKey) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }

        return true;

    }


     /**
     * JWT 토큰에서 권한 목록을 추출합니다. access_token에서만 권한을 추출합니다.
     * @param token JWT 토큰
     * @return 권한 목록
     */
    public List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        // authorities는 JWT의 클레임으로 저장되어 있습니다 그 claim을 파싱하여 권한 목록을 가져옵니다.
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<?> authoritiesList = claims.get("authorities", List.class);

        // 각각의 권한들은 Map<"authority", String> 형태로 저장되어 있습니다. 이걸 spring security의 SimpleGrantedAuthority로 변환합니다.
        return authoritiesList.stream()
                .filter(obj -> obj instanceof Map)
                .map(obj -> (Map<?, ?>) obj)
                .filter(map -> map.containsKey("authority") && map.get("authority") instanceof String)
                .map(map -> new SimpleGrantedAuthority((String) map.get("authority")))
                .collect(Collectors.toList());
    }
}
