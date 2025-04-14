package com.example.board.domain.post.service;

import java.util.List;

import com.example.board.domain.post.dto.PostCreateRequestDto;
import com.example.board.domain.post.dto.PostResponseDto;
import com.example.board.domain.post.dto.PostSearchConditionDto;
import com.example.board.domain.post.dto.PostUpdateRequestDto;

public interface PostService {
    void createPost(PostCreateRequestDto request, Long id);

    PostResponseDto getPostById(Long id);

    void updatePost(Long id, PostUpdateRequestDto request, Long userId);

    void deletePost(Long id, Long userId);

    List<PostResponseDto> searchPosts(PostSearchConditionDto request);

}
