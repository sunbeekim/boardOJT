package com.example.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.board.common.annotation.RequireAuth;
import com.example.board.common.dto.CommonResponseDto;
import com.example.board.common.dto.JwtUserInfo;
import com.example.board.domain.user.dto.CheckRequestDto;
import com.example.board.domain.user.dto.LoginRequestDto;
import com.example.board.domain.user.dto.SignUpRequestDto;
import com.example.board.domain.user.dto.UserUpdateRequestDto;
import com.example.board.domain.user.service.UserService;

import javax.validation.Valid;

// 컨트롤러는 클라이언트의 요청(Request)을 파싱하고,
// 해당 요청을 서비스에 위임한 후, 결과를 HTTP 응답(Response)으로 반환하는 역할만 수행합니다.
// 비즈니스 로직은 서비스 계층에서 처리됩니다.
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto<?>> signup(@Valid @RequestBody SignUpRequestDto request) {
        userService.signup(request);
        return ResponseEntity.status(201).body(CommonResponseDto.success("회원가입이 완료되었습니다.", 200));
    }

    @PostMapping("/check/email")
    public ResponseEntity<CommonResponseDto<?>> checkEmail(@Valid @RequestBody CheckRequestDto request) {
        boolean isDupl = userService.checkEmail(request);
        System.out.println("이메일 중복확인 결과 : " + isDupl);

        if (isDupl == false) {
            return ResponseEntity.status(200).body(CommonResponseDto.success("중복된 이메일이 있습니다.", isDupl));
        } else {
            return ResponseEntity.status(200).body(CommonResponseDto.success("사용가능한 이메일입니다.", isDupl));
        }
    }

    @PostMapping("/check/nickname")
    public ResponseEntity<CommonResponseDto<?>> checkNickname(@Valid @RequestBody CheckRequestDto request) {
        boolean isDupl = userService.checkNickname(request);
        if (isDupl == false) {
            return ResponseEntity.status(200).body(CommonResponseDto.success("중복된 닉네임이 있습니다.", isDupl));
        } else {
            return ResponseEntity.status(200).body(CommonResponseDto.success("사용가능한 닉네임입니다.", isDupl));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<?>> login(@Valid @RequestBody LoginRequestDto request) {
        String token = userService.login(request);
        return ResponseEntity.ok(CommonResponseDto.success("로그인이 완료되었습니다.", token));
    }

    @RequireAuth // 커스텀 어노테이션션
    @PatchMapping("/me")
    public ResponseEntity<CommonResponseDto<?>> update(
            @AuthenticationPrincipal JwtUserInfo userInfo,
            @Valid @RequestBody UserUpdateRequestDto request) {

        userService.update(request, userInfo.getEmail());
        return ResponseEntity.ok(CommonResponseDto.success("회원정보 수정이 완료되었습니다.", request));
    }

    @RequireAuth
    @DeleteMapping("/me")
    public ResponseEntity<CommonResponseDto<?>> delete(@AuthenticationPrincipal JwtUserInfo userInfo) {
        userService.delete(userInfo.getId());
        return ResponseEntity.ok(CommonResponseDto.success("회원탈퇴가 완료되었습니다.", null));
    }

}