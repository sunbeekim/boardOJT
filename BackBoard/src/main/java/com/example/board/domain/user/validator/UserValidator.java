package com.example.board.domain.user.validator;

import org.springframework.stereotype.Component;

import com.example.board.dao.UserMapper;
import com.example.board.exception.DuplicateResourceException;
import com.example.board.dto.SignUpRequestDto;
import com.example.board.dto.UserUpdateRequestDto;
import com.example.board.domain.user.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserMapper userMapper;

    public void validateSignUp(SignUpRequestDto request) {
        validateEmail(request.getEmail());
        validateNickname(request.getNickname());
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
