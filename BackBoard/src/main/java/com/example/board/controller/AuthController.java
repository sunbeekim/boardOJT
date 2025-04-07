package com.example.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.board.dto.CommonResponseDto;
import com.example.board.dto.LoginRequestDto;
import com.example.board.dto.SignUpRequestDto;
import com.example.board.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto<?>> signup(@Valid @RequestBody SignUpRequestDto request) {
        authService.signup(request);
        return ResponseEntity.ok(CommonResponseDto.success("회원가입이 완료되었습니다.", null));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<?>> login(@Valid @RequestBody LoginRequestDto request) {
        String token = authService.login(request);
        return ResponseEntity.ok(CommonResponseDto.success("로그인이 완료되었습니다.", token));
    }
} 