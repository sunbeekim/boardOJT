package com.example.board.controller;

import com.example.board.common.annotation.RequireAuth;
import com.example.board.common.dto.CommonResponseDto;
import com.example.board.common.dto.JwtUserInfo;
import com.example.board.domain.user.service.AdminUserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminUserService adminUserServiceService;

    // 게시판 삭제
    @RequireAuth
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<CommonResponseDto<?>> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal JwtUserInfo userInfo) {
        adminUserServiceService.deletePost(postId, userInfo.getId());
        return ResponseEntity.ok(CommonResponseDto.success("게시글 삭제가 완료되었습니다.", null));
    }

    // 댓글 삭제
    @RequireAuth
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CommonResponseDto<?>> deleteComment(@PathVariable Long commentId,
            @AuthenticationPrincipal JwtUserInfo userInfo) {
        adminUserServiceService.deleteComment(commentId, userInfo.getId());
        return ResponseEntity.ok(CommonResponseDto.success("댓글 삭제가 완료되었습니다.", null));
    }

    // 회원 삭제
    @RequireAuth
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<CommonResponseDto<?>> deleteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal JwtUserInfo userInfo) {
        adminUserServiceService.deleteUser(userId, userInfo.getId());
        return ResponseEntity.ok(CommonResponseDto.success("회원 삭제가 완료되었습니다.", null));
    }

    // 회원 잠금 해제
    @RequireAuth
    @PatchMapping("/users/{userId}/unlock")
    public ResponseEntity<CommonResponseDto<?>> unlockUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal JwtUserInfo userInfo) {
        adminUserServiceService.unlockUser(userId, userInfo.getId());
        return ResponseEntity.ok(CommonResponseDto.success("계정 잠금이 해지되었습니다.", null));
    }
}
