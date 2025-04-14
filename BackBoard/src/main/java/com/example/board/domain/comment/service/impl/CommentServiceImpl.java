package com.example.board.domain.comment.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.board.domain.comment.dao.CommentMapper;
import com.example.board.domain.comment.dto.CommentCreateRequestDto;
import com.example.board.domain.comment.dto.CommentResponseDto;
import com.example.board.domain.comment.dto.CommentUpdateRequestDto;
import com.example.board.domain.comment.service.CommentService;
import com.example.board.domain.post.dao.PostMapper;
import com.example.board.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.board.domain.comment.entity.Comment;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final PostMapper postMapper;

    @Override
    public void insert(CommentCreateRequestDto request, Long postId, Long userId) {
        // 엔티티와 요청데이터 매핑
        Comment comment = Comment.builder()
                .content(request.getContent())
                .postId(postId)
                .userId(userId)
                .build();

        // db 저장
        commentMapper.insert(comment);
        postMapper.increaseCommentCount(postId);
    }

    @Override
    public void update(Long id, CommentUpdateRequestDto request, Long userId) {
        Comment comment = commentMapper.findById(id);
        // 자신이 작성한 것인지 검증증
        if (comment.getUserId().equals(userId)) {
            comment.setContent(request.getContent());
            commentMapper.update(comment);
        } else {
            throw new BusinessException("작성한 사용자가 아닙니다.", "권한 부족", HttpStatus.FORBIDDEN.value());
        }
    }

    @Override
    public void delete(Long id, Long userId) {
        Comment comment = commentMapper.findById(id);
        // 자신이 작성한 것인지 검증증
        if (comment.getUserId().equals(userId)) {
            commentMapper.delete(id);
        } else {
            throw new BusinessException("작성한 사용자가 아닙니다.", "권한 부족", HttpStatus.FORBIDDEN.value());
        }

    }

    @Override
    public List<CommentResponseDto> getFindByPostId(Long postId) {
        try {
            log.info("댓글 조회 postId={}");
            return commentMapper.findByPostId(postId);
        } catch (BusinessException e) {
            throw new BusinessException("조회 에러러", "sdsdssd", HttpStatus.FORBIDDEN.value());
        }
    }
}
