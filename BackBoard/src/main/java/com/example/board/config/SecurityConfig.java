package com.example.board.config;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.board.common.dto.CommonResponseDto;
import com.example.board.security.JwtAuthenticationFilter;
import com.example.board.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    @Autowired
    private ObjectMapper objectMapper;

    // 람다식으로 변경 및 docs/** 경로 허용
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 미사용
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/api/users/login", "/api/users/signup", "/api/users/check/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                        .antMatchers("/api/users/**", "/api/admin/**").authenticated()
                        .antMatchers("/docs/**", "/error/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            CommonResponseDto<?> errorResponse = CommonResponseDto.error(
                                    "인증이 필요합니다.",
                                    "UNAUTHORIZED",
                                    request.getRequestURI());
                            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json;charset=UTF-8");
                            CommonResponseDto<?> errorResponse = CommonResponseDto.error(
                                    "권한이 없습니다.",
                                    "FORBIDDEN",
                                    request.getRequestURI());
                            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                        }))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
                .build();
    }

}