package com.example.board.controller;

import com.example.board.common.annotation.RequireAuth;
import com.example.board.common.dto.CommonResponseDto;
import com.example.board.common.dto.JwtUserInfo;
import com.example.board.domain.post.dto.*;
import com.example.board.domain.post.service.PostService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 등록
    @RequireAuth
    @PostMapping
    public ResponseEntity<CommonResponseDto<?>> register(
            @RequestBody PostCreateRequestDto request,
            @AuthenticationPrincipal JwtUserInfo userInfo) {
        // postService.createPost(request, userInfo.getId());
        return ResponseEntity.ok(CommonResponseDto.success("게시글 등록이 성공하였습니다.", null));
    }

    // // 게시글 단일 조회
    // @GetMapping("/{postId}")
    // public ResponseEntity<CommonResponseDto<?>> getPost(@PathVariable Long
    // postId) {
    // // PostResponseDto post = postService.getPostById(postId);
    // return ResponseEntity.ok(CommonResponseDto.success("게시글 조회가 완료되었습니다.",
    // post));
    // }

    // 게시글 수정
    @RequireAuth
    @PutMapping("/{postId}")
    public ResponseEntity<CommonResponseDto<?>> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequestDto request,
            @AuthenticationPrincipal JwtUserInfo userInfo) {
        // postService.updatePost(postId, request, userInfo.getId());
        return ResponseEntity.ok(CommonResponseDto.success("게시글 수정이 완료되었습니다.", null));
    }

    // 게시글 삭제
    @RequireAuth
    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponseDto<?>> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal JwtUserInfo userInfo) {
        // postService.deletePost(postId, userInfo.getId());
        return ResponseEntity.ok(CommonResponseDto.success("게시글 삭제가 완료되었습니다.", null));
    }

    // 게시글 조건 검색 조회
    // @GetMapping
    // public ResponseEntity<CommonResponseDto<?>> searchPosts(
    // @RequestParam(required = false) String title,
    // @RequestParam(required = false) String nickname,
    // @RequestParam(required = false) String dateFrom,
    // @RequestParam(defaultValue = "0") int page,
    // @RequestParam(defaultValue = "10") int size,
    // @RequestParam(defaultValue = "createAt,DESC") String sort) {
    // // List<PostResponseDto> posts = postService.searchPosts(title, nickname,
    // // dateFrom, page, size, sort);
    // return ResponseEntity.ok(CommonResponseDto.success("게시글 조건 조회가 완료되었습니다.",
    // posts));
    // }
}
