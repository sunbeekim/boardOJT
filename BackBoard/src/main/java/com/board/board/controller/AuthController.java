package com.board.board.controller;

import com.board.board.dto.CommonResponseDto;
import com.board.board.dto.LoginRequestDto;
import com.board.board.dto.SignUpRequestDto;
import com.board.board.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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