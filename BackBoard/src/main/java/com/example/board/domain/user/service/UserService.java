package com.example.board.domain.user.service;

import com.example.board.domain.user.dto.CheckRequestDto;
import com.example.board.domain.user.dto.LoginRequestDto;
import com.example.board.domain.user.dto.SignUpRequestDto;
import com.example.board.domain.user.dto.UserUpdateRequestDto;

public interface UserService {
    void signup(SignUpRequestDto request);

    boolean checkEmail(CheckRequestDto request);

    boolean checkNickname(CheckRequestDto request);

    String login(LoginRequestDto request);

    void update(UserUpdateRequestDto request, Long id);

    void delete(Long id);
}