package com.example.board.domain.user.entity.interfaces;

import com.example.board.common.interfaces.EntityBehavior;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface AdminUserBehavior extends EntityBehavior {

    // 유저 권한 변경
    void changeUserRole(Long id);

    // 로그인 실패 횟수 초기화
    void resetLoginFailCount(Long id);

    // 강제 비밀번호 변경
    void forceChangePassword(user.setPassword(encoder.encode(rawPassword)));

    // 계정 잠금 해제
    void unlockAccount(Long id);
}
