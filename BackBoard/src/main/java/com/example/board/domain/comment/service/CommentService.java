package com.example.board.domain.comment.service;

import java.util.List;

import com.example.board.domain.comment.dto.CommentCreateRequestDto;
import com.example.board.domain.comment.dto.CommentResponseDto;
import com.example.board.domain.comment.dto.CommentUpdateRequestDto;

public interface CommentService {

    void insert(CommentCreateRequestDto request, Long postId, Long userId);

    void update(Long id, CommentUpdateRequestDto request, Long userId);

    void delete(Long id, Long userId);

    List<CommentResponseDto> getFindByPostId(Long postId);
}
