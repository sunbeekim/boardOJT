package com.example.board.domain.user.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import com.example.board.domain.user.entity.interfaces.AdminUserBehavior;
import com.example.board.domain.user.entity.interfaces.UserBehavior;
import com.example.board.domain.user.entity.interfaces.impl.AdminUserBehaviorImpl;
import com.example.board.domain.user.entity.interfaces.impl.UserBehaviorImpl;
import com.example.board.domain.user.enums.UserRole;

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

    // 엔티티 내부에서 new를 이용해서 객체를 생성하는 것은 지양해야 함
    // public UserBehavior userBehavior() {
    // return new UserBehaviorImpl(this);
    // }

    // public AdminUserBehavior adminUserBehavior() {
    // return new AdminUserBehaviorImpl(this);
    // }

}