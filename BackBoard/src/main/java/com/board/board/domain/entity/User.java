package com.board.board.domain.entity;

import com.board.board.domain.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private UserRole role;
    private int loginFailCount;
    private boolean locked;
    private LocalDateTime lastLoginAttempt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean enabled;
} 