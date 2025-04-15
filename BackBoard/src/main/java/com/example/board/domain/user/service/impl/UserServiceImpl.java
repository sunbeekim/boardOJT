package com.example.board.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

import com.example.board.common.factory.BehaviorFactory;
import com.example.board.domain.user.dao.UserMapper;
import com.example.board.domain.user.dto.CheckRequestDto;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserValidator userValidator; // 유저 검증 객체
    private final BehaviorFactory behaviorFactory; // 엔티티 권한별 행동 기능 가져오기 위한 팩토리
    private final UserMapper userMapper; // DB 접근 인터페이스
    private final PasswordEncoder passwordEncoder; // 암호화
    private final JwtUtil jwtUtil; // 로그인 성공 시 토큰 반환

    @Override
    @Transactional
    public void signup(SignUpRequestDto request) {

        log.info("request ={}", request);
        // userValidator.validateEmail(request.getEmail());
        // userValidator.validateNickname(request.getNickname());
        // userValidator.validateEmailFormat(request.getEmail());
        // userValidator.validatePasswordFormat(request.getPassword());
        // userValidator.validateNicknameFormat(request.getNickname());

        // 위의 검증로직 통합한 메서드
        userValidator.validateCreate(request);

        // 엔티티 생성
        User user = createUser(request);

        // 아래와 같이 엔티티와 구현체를 동적으로 할당 가능
        UserBehavior userBehavior = behaviorFactory.wrap(user, UserBehavior.class);
        // 엔티티의 상태 값 변경(비밀번호)
        userBehavior.register(request.getPassword(), passwordEncoder);

        // 저장
        try {
            userMapper.save(user);
        } catch (PersistenceException e) {
            throw e;
        }

    }

    public User createUser(SignUpRequestDto request) { // 엔티티 생성성
        User user = User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .role(UserRole.ROLE_USER)
                .enabled(true)
                .build();

        return user;
    }

    @Override
    public String login(LoginRequestDto request) {
        User user = userMapper.findByEmail(request.getEmail());
        UserBehavior userBehavior = behaviorFactory.wrap(user, UserBehavior.class);
        try {
            userValidator.validateExistenceFilter(user); // 계정 존재여부 검증
            userValidator.verifyAccount(user); // 계정 잠금여부 확인
            userValidator.validateLogin(request, passwordEncoder); // 비밀번호 일치 검증증
            userBehavior.handleLoginSuccess(); // 성공 시 엔티티 상태변경(실패 상태 초기화)
            try {
                userMapper.updateLoginFailCount(user.getId(), // DB 엔티티 상태 업데이트(성공)
                        user.getLoginFailCount(),
                        user.isLocked());
            } catch (PersistenceException e) {
                throw e;
            }
        } catch (UnauthorizedException e) {
            userBehavior.handleLoginFailure(); // 실패 시 엔티티 상태변경
            try {
                userMapper.updateLoginFailCount(user.getId(), // DB 엔티티 상태 업데이트(실패)
                        user.getLoginFailCount(),
                        user.isLocked());
            } catch (PersistenceException ex) {
                throw ex;
            }
            throw e;
        }

        return jwtUtil.createToken(user);

    }

    @Override
    @Transactional
    public void update(UserUpdateRequestDto request, Long id) {
        User user = userMapper.findById(id);
        // 검증
        userValidator.validateUpdate(request, user);
        // 엔티티 행동 기능 가져오기
        UserBehavior userBehavior = behaviorFactory.wrap(user, UserBehavior.class);
        userBehavior.changeNickname(request.getNickname()); // 상태 값 변경
        userBehavior.changePassword(request.getPassword(), passwordEncoder); // 비밀번호 암호화
        try {
            userMapper.update(user); // DB users 테이블 업데이트
        } catch (PersistenceException e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userMapper.findById(id);
        userValidator.validateDelete(id, user);
        try {
            userMapper.delete(id);
        } catch (PersistenceException e) {
            throw e;
        }
    }

    @Override
    public boolean checkEmail(CheckRequestDto request) {
        User user = userMapper.findByEmail(request.getEmail());
        userValidator.validateEmailFormat(request.getEmail()); // 형식 검증
        return userValidator.validateBooleanEmail(user);
    }

    @Override
    public boolean checkNickname(CheckRequestDto request) {
        User user = userMapper.findByEmail(request.getNickname());
        userValidator.validateNicknameFormat(request.getNickname()); // 형식 검증
        return userValidator.validateBooleanNickname(user);
    }
}