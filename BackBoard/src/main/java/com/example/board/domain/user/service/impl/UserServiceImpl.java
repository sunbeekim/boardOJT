package com.example.board.domain.user.service.impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.common.factory.BehaviorFactory;
import com.example.board.domain.user.dao.UserMapper;
import com.example.board.domain.user.dto.CheckRequestDto;
import com.example.board.domain.user.dto.LoginRequestDto;
import com.example.board.domain.user.dto.SignUpRequestDto;
import com.example.board.domain.user.dto.UserUpdateRequestDto;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.interfaces.UserBehavior;
import com.example.board.domain.user.entity.provider.UserBehaviorProvider;
import com.example.board.domain.user.enums.UserRole;
import com.example.board.domain.user.service.UserService;
import com.example.board.domain.user.validator.UserValidator;
import com.example.board.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserValidator userValidator; // 유저 검증 객체
    private final BehaviorFactory behaviorFactory; // 엔티티 권한별 행동 기능 가져오기 위한 팩토리
    private final UserMapper userMapper; // DB 접근 인터페이스
    private final PasswordEncoder passwordEncoder; // 암호화
    private final JwtUtil jwtUtil; // 로그인 성공 시 토큰 반환
    private final UserBehaviorProvider userBehaviorProvider;

    @Override
    @Transactional
    public void signup(SignUpRequestDto request) {

        log.info("회원가입 요청 - request: {}", request);
        // userValidator.validateEmail(request.getEmail());
        // userValidator.validateNickname(request.getNickname());
        // userValidator.validateEmailFormat(request.getEmail());
        // userValidator.validatePasswordFormat(request.getPassword());
        // userValidator.validateNicknameFormat(request.getNickname());

        // 위의 검증로직 통합한 공통 메서드
        userValidator.validateCreate(request);

        // 엔티티 생성
        User user = createUser(request);

        // 팩토리 로직을 몰라도 되게 하기 위한 중간 계층
        // UserBehavior userBehavior = userBehaviorProvider.get(user);

        UserBehavior userBehavior = behaviorFactory.wrap(user, UserBehavior.class);

        // 비밀번호 암호화 및 초기 상태 설정
        userBehavior.register(request.getPassword(), passwordEncoder);

        // 저장
        try {
            userMapper.save(user);
            log.info("회원가입 완료 - userId: {}", user.getId());
        } catch (Exception e) {
            log.error("회원가입 실패 - email: {}", request.getEmail(), e);
            throw e;
        }
    }

    private User createUser(SignUpRequestDto request) {
        return User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .role(UserRole.ROLE_USER)
                .enabled(true)
                .build();
    }

    @Override
    public String login(LoginRequestDto request) {
        log.info("로그인 시도 - email: {}", request.getEmail());

        User user = userMapper.findByEmail(request.getEmail());
        UserBehavior userBehavior = behaviorFactory.wrap(user, UserBehavior.class);

        try {

            // 계정 존재 여부 검증
            userValidator.validateExistenceFilter(user, User.class);

            // 계정 잠금 여부 확인
            userValidator.verifyAccount(user);

            // 비밀번호 일치 검증
            userValidator.validateLogin(request, passwordEncoder);

            // 로그인 성공 처리
            userBehavior.handleLoginSuccess();
            userMapper.updateLoginFailCount(user.getId(), user.getLoginFailCount(), user.isLocked());

            log.info("로그인 성공 - userId: {}", user.getId());
            return jwtUtil.createToken(user);

        } catch (Exception e) {
            // 로그인 실패 처리
            userBehavior.handleLoginFailure();
            userMapper.updateLoginFailCount(user.getId(), user.getLoginFailCount(), user.isLocked());

            log.warn("로그인 실패 - email: {}", request.getEmail());
            throw e;
        }
    }

    @Override
    @Transactional
    public void update(UserUpdateRequestDto request, Long id) {
        log.info("회원정보 수정 요청 - userId: {}", id);

        User user = userMapper.findById(id);

        // 수정 검증
        userValidator.validateUpdate(request, user);

        // 엔티티 상태 변경
        UserBehavior userBehavior = behaviorFactory.wrap(user, UserBehavior.class);
        userBehavior.changeNickname(request.getNickname()); // 상태 값 변경
        userBehavior.changePassword(request.getPassword(), passwordEncoder); // 비밀번호 암호화
        try {
            userMapper.update(user);
            log.info("회원정보 수정 완료 - userId: {}", id);
        } catch (Exception e) {
            log.error("회원정보 수정 실패 - userId: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("회원탈퇴 요청 - userId: {}", id);

        User user = userMapper.findById(id);
        userValidator.validateDelete(id, user);

        try {
            userMapper.delete(id);
            log.info("회원탈퇴 완료 - userId: {}", id);
        } catch (Exception e) {
            log.error("회원탈퇴 실패 - userId: {}", id, e);
            throw e;
        }
    }

    @Override
    public boolean checkEmail(CheckRequestDto request) {
        log.info("이메일 중복 확인 - email: {}", request.getEmail());

        userValidator.validateEmailFormat(request.getEmail());
        User user = userMapper.findByEmail(request.getEmail());

        boolean isAvailable = userValidator.validateBooleanEmail(user);
        log.info("이메일 중복 확인 결과 - email: {}, isAvailable: {}", request.getEmail(), isAvailable);
        return isAvailable;
    }

    @Override
    public boolean checkNickname(CheckRequestDto request) {
        log.info("닉네임 중복 확인 - nickname: {}", request.getNickname());

        userValidator.validateNicknameFormat(request.getNickname());
        User user = userMapper.findByNickname(request.getNickname());

        boolean isAvailable = userValidator.validateBooleanNickname(user);
        log.info("닉네임 중복 확인 결과 - nickname: {}, isAvailable: {}", request.getNickname(), isAvailable);
        return isAvailable;
    }
}