package com.example.board.domain.user.entity.interfaces;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.board.common.interfaces.EntityBehaviorInterface;
import com.example.board.domain.user.entity.User;

public interface AdminUserBehavior extends EntityBehaviorInterface {

    // 권한 확인
    boolean checkPermission();

    // 잠금 계정 해지
    void unlock(User user);

    // 유저 권한 변경
    void changeUserRole();

    // 로그인 실패 횟수 초기화
    void resetLoginFailCount(User user);

    // 유저 비밀번호 변경(관리자)
    void changeForcePassword(String rawPassword, PasswordEncoder encoder);

}
