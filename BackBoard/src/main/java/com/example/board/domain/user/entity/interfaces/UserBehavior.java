package com.example.board.domain.user.entity.interfaces;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.board.common.interfaces.EntityBehavior;

// 엔티티가 갖는 행동 명시
public interface UserBehavior extends EntityBehavior {
    // 회원가입 시 비밀번호 암호화
    void register(String rawPassword, PasswordEncoder encoder);

    // 로그인 시 비밀번호 일치 검증
    boolean verifyPassword(String rawPassword, PasswordEncoder encoder);

    // 닉네임 변경
    void changeNickname(String newNickname);

    // 비밀번호 변경
    void changePassword(String rawPassword, PasswordEncoder encoder);

}