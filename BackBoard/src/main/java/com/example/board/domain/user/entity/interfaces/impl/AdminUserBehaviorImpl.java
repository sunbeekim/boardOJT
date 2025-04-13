package com.example.board.domain.user.entity.interfaces.impl;

public class UserBAdminUserBehaviorImpl implements AdminUserBehavior {
    private final User user;

     public UserBAdminUserBehaviorImpl(User user) {
        this.user = user;
    }

    // 유저 권한 변경
    void changeUserRole(String newRole){
        // 특정 유저 엔티티의 권한 상태를 변경
        user.setRole(newRole);
    }

    // 로그인 실패 횟수 초기화
    void resetLoginFailCount(){
        // 특정 유저 엔티티의 실패 횟수 초기화
        user.setLoginFailCount(0);
    }

    // 강제 비밀번호 변경
    void forceChangePassword(String rawPassword, PasswordEncoder encoder){
        // 특정 유저 엔티티의 비밀번호 초기화
        user.setPassword(encoder.encode(rawPassword));
    }

    // 계정 잠금 해제
    void unlockAccount(){
        // 특정 유저 엔티티의 잠금 상태 해제
        user.setLocked(false);
    }
}