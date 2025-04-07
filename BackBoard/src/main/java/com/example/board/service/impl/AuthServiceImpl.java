package com.example.board.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.dao.UserMapper;
import com.example.board.domain.entity.User;
import com.example.board.domain.enums.UserRole;
import com.example.board.dto.LoginRequestDto;
import com.example.board.dto.SignUpRequestDto;
import com.example.board.exception.AccountLockedException;
import com.example.board.exception.DuplicateResourceException;
import com.example.board.exception.UnauthorizedException;
import com.example.board.service.AuthService;
import com.example.board.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void signup(SignUpRequestDto request) {
        // 이메일 중복 체크
        if (userMapper.findByEmail(request.getEmail()) != null) {
            throw new DuplicateResourceException("이미 사용 중인 이메일입니다.");
        }

        // 닉네임 중복 체크
        if (userMapper.findByNickname(request.getNickname()) != null) {
            throw new DuplicateResourceException("이미 사용 중인 닉네임입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 사용자 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .role(UserRole.ROLE_USER)
                .enabled(true)
                .build();

        userMapper.save(user);
    }

    @Override
    //@Transactional
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
    
} 