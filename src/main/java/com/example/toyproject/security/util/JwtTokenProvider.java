package com.example.toyproject.security.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long tokenValidityInMilliseconds;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    public String createToken(Authentication authentication) {
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
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        // authorities는 JWT의 클레임으로 저장되어 있습니다 그 claim을 파싱하여 권한 목록을 가져옵니다.
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
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
