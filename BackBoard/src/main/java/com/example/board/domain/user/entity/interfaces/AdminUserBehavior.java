package com.example.board.domain.user.entity.interfaces;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.board.common.interfaces.EntityBehaviorInterface;

public interface AdminUserBehavior extends EntityBehaviorInterface {

    // 권한 확인
    boolean checkPermission();

    // 잠금 계정 해지
    void unlock();

    // 유저 권한 변경
    void changeUserRole(Long id);

    // 로그인 실패 횟수 초기화
    void resetLoginFailCount(Long id);

    // 유저 비밀번호 변경(관리자)
    void changeForcePassword(String rawPassword, PasswordEncoder encoder);

}
