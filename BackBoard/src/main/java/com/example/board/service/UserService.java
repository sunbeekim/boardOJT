package com.example.board.service;

import com.example.board.domain.user.dto.LoginRequestDto;
import com.example.board.domain.user.dto.SignUpRequestDto;

public interface UserService {
    void signup(SignUpRequestDto request);
    String login(LoginRequestDto request);
} 