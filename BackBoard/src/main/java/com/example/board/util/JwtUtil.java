package com.example.board.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.board.common.dto.JwtUserInfo;
import com.example.board.config.JwtConfig;
import com.example.board.domain.user.entity.User;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtConfig jwtConfig;
    private Key key;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    // JWT 생성
    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("id", user.getId());
        claims.put("nickname", user.getNickname());
        claims.put("role", user.getRole().name());

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtConfig.getExpiration());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 인증 객체 반환
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        JwtUserInfo userInfo = new JwtUserInfo(
                claims.get("id", Long.class),
                claims.getSubject(),
                claims.get("nickname", String.class));

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(claims.get("role", String.class)));

        return new UsernamePasswordAuthenticationToken(userInfo, token, authorities);
    }

    // HTTP 요청 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtConfig.getHeader());
        if (bearerToken != null && bearerToken.startsWith(jwtConfig.getPrefix())) {
            return bearerToken.substring(jwtConfig.getPrefix().length());
        }
        return null;
    }

    // 트큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw e;
        }
    }

    // 오버헤드용 토큰 추출 메서드
    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(jwtConfig.getPrefix())) {
            return bearerToken.substring(jwtConfig.getPrefix().length());
        }
        return null;
    }

    // Claims 정보 추출용
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return getClaims(token).get("id", Long.class);
    }

    public String getNicknameFromToken(String token) {
        return getClaims(token).get("nickname", String.class);
    }

    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }

    // 내부 공용 처리
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}