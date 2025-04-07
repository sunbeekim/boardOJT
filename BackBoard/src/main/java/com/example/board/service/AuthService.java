package com.example.board.service;

import com.example.board.dto.LoginRequestDto;
import com.example.board.dto.SignUpRequestDto;

public interface AuthService {
    void signup(SignUpRequestDto request);
    String login(LoginRequestDto request);
} 