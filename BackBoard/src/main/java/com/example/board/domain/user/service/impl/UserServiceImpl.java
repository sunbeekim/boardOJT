package com.example.board.domain.user.service.impl;

import lombok.RequiredArgsConstructor;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

import com.example.board.common.factory.BehaviorFactory;
import com.example.board.dao.UserMapper;
import com.example.board.domain.user.dto.LoginRequestDto;
import com.example.board.domain.user.dto.SignUpRequestDto;
import com.example.board.domain.user.dto.UserUpdateRequestDto;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.interfaces.UserBehavior;
import com.example.board.domain.user.enums.UserRole;
import com.example.board.exception.TableNotFoundException;
import com.example.board.exception.UnauthorizedException;
import com.example.board.domain.user.service.UserService;
import com.example.board.util.JwtUtil;
import com.example.board.domain.user.validator.UserValidator;
import com.example.board.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserValidator userValidator;
    private final BehaviorFactory behaviorFactory;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void signup(SignUpRequestDto request) {
        try {
            // 1. 유효성 검증 (DuplicateResourceException 발생)
            userValidator.validateSignUp(request);

            // 2. 엔티티 생성
            User user = User.builder()
                    .email(request.getEmail())
                    .nickname(request.getNickname())
                    .role(UserRole.ROLE_USER)
                    .enabled(true)
                    .build();

            // 3. 비즈니스 로직 실행
            UserBehavior userBehavior = behaviorFactory.wrap(user, UserBehavior.class);
            userBehavior.register(request.getPassword(), passwordEncoder);

            // 4. 저장
            userMapper.save(user);
        } catch (PersistenceException e) {
            if (e.getMessage().contains("doesn't exist") || e.getMessage().contains("table")) {
                throw new TableNotFoundException("users 테이블이 존재하지 않습니다: " + e.getMessage());
            }
            throw new BusinessException("데이터베이스 오류가 발생했습니다.", "DB_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public String login(LoginRequestDto request) {
        try {
            User user = userMapper.findByEmail(request.getEmail());
            if (user == null) {
                throw new UnauthorizedException("존재하지 않는 아이디입니다.");
            }

            UserBehavior userBehavior = behaviorFactory.wrap(user, UserBehavior.class);

            try {
                userValidator.validateLogin(request, passwordEncoder);
                userBehavior.handleLoginSuccess(); // 성공 시 처리
                userMapper.updateLoginFailCount(user.getId(),
                        user.getLoginFailCount(),
                        user.isLocked());
            } catch (UnauthorizedException e) {
                userBehavior.handleLoginFailure(); // 실패 시 처리
                userMapper.updateLoginFailCount(user.getId(),
                        user.getLoginFailCount(),
                        user.isLocked());
                throw e; // 다시 예외 던짐
            }

            return jwtUtil.createToken(user);
        } catch (PersistenceException e) {
            throw new BusinessException("데이터베이스 오류가 발생했습니다.", "DB_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    @Transactional
    public void update(UserUpdateRequestDto request, String email) {
        try {
            User user = userMapper.findByEmail(email);
            if (user == null) {
                throw new UnauthorizedException("로그인된 사용자를 찾을 수 없습니다.");
            }

            userValidator.validateUpdate(request, user);

            UserBehavior userBehavior = behaviorFactory.wrap(user, UserBehavior.class);
            userBehavior.changeNickname(request.getNickname());
            userBehavior.changePassword(request.getPassword(), passwordEncoder);

            userMapper.update(user);
        } catch (PersistenceException e) {
            throw new BusinessException("데이터베이스 오류가 발생했습니다.", "DB_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            User user = userMapper.findById(id);
            if (user == null) {
                throw new UnauthorizedException("로그인된 사용자를 찾을 수 없습니다.");
            }
            userMapper.delete(id);
        } catch (PersistenceException e) {
            throw new BusinessException("데이터베이스 오류가 발생했습니다.", "DB_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}