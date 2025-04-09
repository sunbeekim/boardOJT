package com.example.board.domain.user.entity;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.board.domain.user.entity.interfaces.UserBehavior;
import java.time.LocalDateTime;

public class UserBehaviorImpl implements UserBehavior {
    private final User user;
    private static final int MAX_LOGIN_FAIL_COUNT = 5;

    public UserBehaviorImpl(User user) {
        this.user = user;
    }

    @Override
    public void register(String rawPassword, PasswordEncoder encoder) {
        // 비밀번호 암호화 및 초기 상태 설정
        user.setPassword(encoder.encode(rawPassword));
        user.setEnabled(true);
        user.setLoginFailCount(0);
        user.setLocked(false);
    }

    @Override
    public boolean verifyPassword(String rawPassword, PasswordEncoder encoder) {
        // 계정 잠금 상태 확인
        if (user.isLocked()) {
            throw new AccountLockedException("계정이 잠겼습니다.");
        }
        return encoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public void changeNickname(String newNickname) {
        // 닉네임 변경 로직
        user.setNickname(newNickname);
    }

    @Override
    public void changePassword(String rawPassword, PasswordEncoder encoder) {
        // 비밀번호 변경 로직
        user.setPassword(encoder.encode(rawPassword));
    }

    @Override
    public void handleLoginSuccess() {
        // 로그인 성공 시 상태 초기화
        user.setLoginFailCount(0);
        user.setLastLoginAttempt(LocalDateTime.now());
    }

    @Override
    public void handleLoginFailure() {
        // 로그인 실패 시 상태 업데이트
        int newFailCount = user.getLoginFailCount() + 1;
        user.setLoginFailCount(newFailCount);
        user.setLastLoginAttempt(LocalDateTime.now());
        
        if (newFailCount >= MAX_LOGIN_FAIL_COUNT) {
            user.setLocked(true);
        }
    }
}
