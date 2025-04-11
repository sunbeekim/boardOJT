package com.example.board.domain.user.validator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.board.common.validator.DomainValidator;
import com.example.board.dao.UserMapper;
import com.example.board.exception.AccountLockedException;
import com.example.board.exception.DuplicateResourceException;
import com.example.board.exception.UnauthorizedException;
import com.example.board.domain.user.dto.LoginRequestDto;
import com.example.board.domain.user.dto.SignUpRequestDto;
import com.example.board.domain.user.dto.UserUpdateRequestDto;
import com.example.board.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

// 엔티티의 상태변화에 관여하지 않고 오직 검증만만
@Component
@RequiredArgsConstructor
public class UserValidator extends DomainValidator { // 아직 확정아니라서 interface를 implement한 abstract로 확장
    private final UserMapper userMapper;

    public void validateSignUp(SignUpRequestDto request) {
        validateEmail(request.getEmail());
        validateNickname(request.getNickname());
    }

    public void validateLogin(LoginRequestDto request, PasswordEncoder encoder) {

        User user = userMapper.findByEmail(request.getEmail());
        String rawPassword = request.getPassword();
        verifyAccount(user);
        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void verifyAccount(User user) {
        // 계정 잠금 상태 확인
        if (user.isLocked()) {
            throw new AccountLockedException("계정이 잠겼습니다.");
        }
    }

    private void validateEmail(String email) {
        // 이메일 중복 검증
        if (userMapper.findByEmail(email) != null) {
            throw new DuplicateResourceException("이미 사용 중인 이메일입니다.");
        }
    }

    private void validateNickname(String nickname) {
        // 닉네임 중복 검증
        if (userMapper.findByNickname(nickname) != null) {
            throw new DuplicateResourceException("이미 사용 중인 닉네임입니다.");
        }
    }

    public void validateUpdate(UserUpdateRequestDto request, User currentUser) {
        // 현재 사용자의 닉네임이 아닌 경우에만 중복 검증
        if (!request.getNickname().equals(currentUser.getNickname())) {
            validateNickname(request.getNickname());
        }
    }

}