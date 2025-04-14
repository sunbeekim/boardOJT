package com.example.board.domain.comment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.board.domain.comment.dto.CommentResponseDto;
import com.example.board.domain.comment.entity.Comment;

@Mapper
public interface CommentMapper {

    void insert(Comment comment);

    Comment findById(@Param("id") Long id);

    void update(Comment comment);

    void deleteById(@Param("id") Long id);

    List<CommentResponseDto> findByPostId(
            @Param("postId") Long userId);

    public static CommentResponseDto toResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
