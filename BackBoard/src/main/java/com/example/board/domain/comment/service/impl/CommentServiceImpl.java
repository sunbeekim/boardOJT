package com.example.board.domain.comment.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.board.domain.comment.dao.CommentMapper;
import com.example.board.domain.comment.dto.CommentCreateRequestDto;
import com.example.board.domain.comment.dto.CommentResponseDto;
import com.example.board.domain.comment.dto.CommentUpdateRequestDto;
import com.example.board.domain.comment.entity.Comment;
import com.example.board.domain.comment.validator.CommentValidator;
import com.example.board.domain.comment.service.CommentService;
import com.example.board.domain.post.dao.PostMapper;
import com.example.board.domain.post.entity.Post;
import com.example.board.domain.post.entity.validator.PostValidator;
import com.example.board.domain.user.dao.UserMapper;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.validator.UserValidator;
import com.example.board.exception.ForbiddenException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final PostValidator postValidator;
    private final CommentValidator commentValidator;

    @Override
    public void insert(CommentCreateRequestDto request, Long postId, Long userId) {
        log.info("댓글 등록 요청 - postId: {}, userId: {}", postId, userId);

        // 계정 존재 여부 검증
        User user = userMapper.findById(userId);
        userValidator.validateExistenceFilter(user, User.class);

        // 게시글 존재 여부 검증
        Post post = postMapper.findById(postId);
        postValidator.validateExistenceFilter(post, Post.class);

        // 엔티티와 요청데이터 매핑
        Comment comment = Comment.builder()
                .content(request.getContent())
                .postId(postId)
                .userId(userId)
                .build();

        // db 저장
        commentMapper.insert(comment);
        postMapper.increaseCommentCount(postId);
        log.info("댓글 등록 완료 - commentId: {}", comment.getId());
    }

    @Override
    public void update(Long id, CommentUpdateRequestDto request, Long userId) {
        log.info("댓글 수정 요청 - commentId: {}, userId: {}", id, userId);

        // 유저가 작성한 게시판 존재 여부 검증
        Post post = postMapper.findByUserId(userId);
        postValidator.validateExistenceFilter(post, Post.class);

        // 댓글 존재 여부 검증
        Comment comment = commentMapper.findById(id);
        commentValidator.validateExistenceFilter(comment, Comment.class);

        // 작성자 본인 여부 검증
        if (comment.getUserId() != userId) {
            log.warn("댓글 수정 권한 없음 - commentId: {}, userId: {}", id, userId);
            throw new ForbiddenException("댓글 작성자만 수정할 수 있습니다.");
        }

        comment.setContent(request.getContent());
        commentMapper.update(comment);
        log.info("댓글 수정 완료 - commentId: {}", id);
    }

    @Override
    public void delete(Long id, Long userId) {
        log.info("댓글 삭제 요청 - commentId: {}, userId: {}", id, userId);

        // 유저가 작성한 게시판 존재 여부 검증
        Post post = postMapper.findByUserId(userId);
        postValidator.validateExistenceFilter(post, Post.class);

        // 댓글 존재 여부 검증
        Comment comment = commentMapper.findById(id);
        log.info("댓글 삭제 중 comment data: {}", comment);
        commentValidator.validateExistenceFilter(comment, Comment.class);

        // 작성자 본인 여부 검증
        if (comment.getUserId() != userId) {
            log.warn("댓글 수정 권한 없음 - commentId: {}, userId: {}", id, userId);
            throw new ForbiddenException("댓글 작성자만 수정할 수 있습니다.");
        }

        commentMapper.delete(id);
        log.info("댓글 삭제 완료 - commentId: {}", id);
    }

    @Override
    public List<CommentResponseDto> getFindByPostId(Long postId) {
        log.info("댓글 목록 조회 요청 - postId: {}", postId);

        // 게시글 존재 여부 검증
        Post post = postMapper.findById(postId);
        postValidator.validateExistenceFilter(post, Post.class);

        List<CommentResponseDto> comments = commentMapper.findByPostId(postId);
        log.info("댓글 목록 조회 완료 - postId: {}, 조회된 댓글 수: {}", postId, comments.size());
        return comments;
    }
}
