package com.example.board.service.impl;

import lombok.RequiredArgsConstructor;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.common.factory.BehaviorFactory;
import com.example.board.dao.UserMapper;
import com.example.board.domain.user.dto.LoginRequestDto;
import com.example.board.domain.user.dto.SignUpRequestDto;
import com.example.board.domain.user.dto.UserUpdateRequestDto;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.interfaces.UserBehavior;
import com.example.board.domain.user.enums.UserRole;
import com.example.board.exception.AccountLockedException;
import com.example.board.exception.TableNotFoundException;
import com.example.board.exception.UnauthorizedException;
import com.example.board.service.UserService;
import com.example.board.util.JwtUtil;
import com.example.board.domain.user.validator.UserValidator;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserValidator userValidator;
    private final BehaviorFactory behaviorFactory;

    @Override
    @Transactional
    public void signup(SignUpRequestDto request) {
        try {
            userValidator.validateUniquenessEmail(request.getEmail());
            userValidator.validateUniquenessNickname(request.getNickname());
            // 사용자 생성
            User user = User.builder()
                    .email(request.getEmail())
                    .nickname(request.getNickname())
                    .role(UserRole.ROLE_USER)
                    .enabled(true)
                    .build();

            // UserBehavior를 통해 비밀번호 암호화 처리
            user.behavior().register(request.getPassword(), passwordEncoder);

            userMapper.save(user);
        }
        // 테이블 Not Found 예외처리
        catch (PersistenceException e) {
            if (e.getMessage().contains("doesn't exist") || e.getMessage().contains("table")) {
                throw new TableNotFoundException("users 테이블이 존재하지 않습니다: " + e.getMessage());
            }
        }
    }

    @Override
    // @Transactional
    public String login(LoginRequestDto request) {
        // 사용자 조회
        User user = userMapper.findByEmail(request.getEmail());
        if (user == null) {

            throw new UnauthorizedException("(이메일) 또는 비밀번호가 일치하지 않습니다.");
        }

        // 계정 잠금 확인
        if (user.isLocked()) {
            throw new AccountLockedException("계정이 잠겨있습니다. 관리자에게 문의하세요.");
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // 로그인 실패 횟수 증가
            int failCount = user.getLoginFailCount() + 1;
            // 로그인 연속 실패 5회
            boolean locked = failCount >= 5;

            userMapper.updateLoginFailCount(user.getId(), failCount, locked);
            if (locked) {
                throw new AccountLockedException("비밀번호 5회 오류로 계정이 잠겼습니다. 관리자에게 문의하세요.");
            }

            throw new UnauthorizedException("이메일 또는 (비밀번호)가 일치하지 않습니다.");
        }

        // 로그인 성공 시 실패 횟수 초기화
        userMapper.updateLoginFailCount(user.getId(), 0, false);

        // JWT 토큰 생성
        return jwtUtil.createToken(user);
    }

    @Override
    public void update(UserUpdateRequestDto request) {
        userValidator.validateUniquenessNickname(request.getNickname());
        User user = userMapper.findByEmail(request.getEmail());
        if (user == null) {
            throw new UnauthorizedException("로그인된 사용자를 찾을 수 없습니다.");
        }

        UserBehavior userBehavior = behaviorFactory.wrap(user, UserBehavior.class);

        userBehavior.changeNickname(request.getNickname());
        userBehavior.changePassword(request.getPassword(), passwordEncoder);

        userMapper.update(user);
    }
}