package com.example.board.domain.user.validator;

import org.springframework.stereotype.Component;

import com.example.board.dao.UserMapper;
import com.example.board.exception.DuplicateResourceException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserMapper userMapper;

    public void validateUniquenessEmail(String email) {
        if (userMapper.findByEmail(email) != null)
            throw new DuplicateResourceException("이미 사용 중인 이메일입니다.");
    }

    public void validateUniquenessNickname(String nickname) {
        if (userMapper.findByNickname(nickname) != null)
            throw new DuplicateResourceException("이미 사용 중인 닉네임입니다.");
    }
}
