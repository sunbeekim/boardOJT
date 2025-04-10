package com.example.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.board.common.dto.CommonResponseDto;
import com.example.board.common.dto.JwtUserInfo;
import com.example.board.domain.user.dto.LoginRequestDto;
import com.example.board.domain.user.dto.SignUpRequestDto;
import com.example.board.domain.user.dto.UserUpdateRequestDto;
import com.example.board.domain.user.service.UserService;
import com.example.board.exception.UnauthorizedException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto<?>> signup(@Valid @RequestBody SignUpRequestDto request) {
        userService.signup(request);
        return ResponseEntity.ok(CommonResponseDto.success("회원가입이 완료되었습니다.", 200));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<?>> login(@Valid @RequestBody LoginRequestDto request) {
        String token = userService.login(request);
        return ResponseEntity.ok(CommonResponseDto.success("로그인이 완료되었습니다.", token));
    }

    @PatchMapping("/me")
    public ResponseEntity<CommonResponseDto<?>> update(
            @AuthenticationPrincipal JwtUserInfo userInfo,
            @Valid @RequestBody UserUpdateRequestDto request) {

        if (userInfo == null) {
            throw new UnauthorizedException("인증 정보가 없습니다");
        }

        userService.update(request, userInfo.getEmail());
        return ResponseEntity.ok(CommonResponseDto.success("회원정보 수정이 완료되었습니다.", request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<CommonResponseDto<?>> delete(@AuthenticationPrincipal JwtUserInfo userInfo) {
        if (userInfo == null) {
            throw new UnauthorizedException("인증 정보가 없습니다");
        }
        userService.delete(userInfo.getId());
        return ResponseEntity.ok(CommonResponseDto.success("회원탈퇴가 완료되었습니다.", null));
    }

}