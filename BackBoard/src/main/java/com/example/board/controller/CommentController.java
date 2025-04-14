package com.example.board.controller;

import com.example.board.common.annotation.RequireAuth;
import com.example.board.common.dto.CommonResponseDto;
import com.example.board.common.dto.JwtUserInfo;
import com.example.board.domain.comment.dto.*;
import com.example.board.domain.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @RequireAuth
    @PostMapping("/{postId}")
    public ResponseEntity<CommonResponseDto<?>> registerComment(
            @RequestBody CommentCreateRequestDto request,
            @PathVariable Long postId,
            @AuthenticationPrincipal JwtUserInfo userInfo) {
        commentService.insert(request, postId, userInfo.getId());
        return ResponseEntity.ok(CommonResponseDto.success("댓글이 등록되었습니다.", null));
    }

    // 특정 게시글의 댓글 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<CommonResponseDto<?>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentResponseDto> comments = commentService.getFindByPostId(postId);
        return ResponseEntity.ok(CommonResponseDto.success("댓글 조회가 완료되었습니다.",
                comments));
    }

    // 댓글 수정
    @RequireAuth
    @PutMapping("/{commentId}")
    public ResponseEntity<CommonResponseDto<?>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequestDto request,
            @AuthenticationPrincipal JwtUserInfo userInfo) {
        commentService.update(commentId, request, userInfo.getId());
        return ResponseEntity.ok(CommonResponseDto.success("댓글이 수정되었습니다.", null));
    }

    // 댓글 삭제
    @RequireAuth
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponseDto<?>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal JwtUserInfo userInfo) {
        commentService.delete(commentId, userInfo.getId());
        return ResponseEntity.ok(CommonResponseDto.success("댓글이 삭제되었습니다.", null));
    }
}
