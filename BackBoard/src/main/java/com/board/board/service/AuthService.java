package com.board.board.service;

import com.board.board.dto.LoginRequestDto;
import com.board.board.dto.SignUpRequestDto;

public interface AuthService {
    void signup(SignUpRequestDto request);
    String login(LoginRequestDto request);
} 