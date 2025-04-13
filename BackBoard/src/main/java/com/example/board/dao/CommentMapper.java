package com.example.board.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.board.domain.comment.dto.CommentResponseDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper {

    // 댓글 등록
    void insertComment(@Param("postId") Long postId, @Param("userId") Long userId, @Param("content") String content);

    // 게시글의 모든 댓글 조회
    List<CommentResponseDto> getCommentsByPostId(@Param("postId") Long postId, @Param("currentUserId") Long currentUserId);

}
