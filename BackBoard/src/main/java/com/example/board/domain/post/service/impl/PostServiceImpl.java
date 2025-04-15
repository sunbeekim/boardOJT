package com.example.board.domain.post.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.board.domain.post.dao.PostMapper;
import com.example.board.domain.post.dto.PostCreateRequestDto;
import com.example.board.domain.post.dto.PostResponseDto;
import com.example.board.domain.post.dto.PostSearchConditionDto;
import com.example.board.domain.post.dto.PostUpdateRequestDto;
import com.example.board.domain.post.entity.Post;
import com.example.board.domain.post.entity.validator.PostValidator;
import com.example.board.domain.post.service.PostService;
import com.example.board.domain.user.dao.UserMapper;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.validator.UserValidator;
import com.example.board.exception.ForbiddenException;
import com.example.board.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostValidator postValidator;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Override
    public void createPost(PostCreateRequestDto request, Long userId) {
        log.info("게시글 등록 요청 - userId: {}, title: {}", userId, request.getTitle());
        
        // 계정 존재 여부 검증
        User user = userMapper.findById(userId);
        userValidator.validateExistenceFilter(user);
        
        // 엔티티와 요청데이터 매핑
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .userId(userId)
                .build();
        
        // 게시글 생성 검증
        postValidator.validateCreate(post, userId);
        
        // db 저장
        postMapper.insert(post);
        log.info("게시글 등록 완료 - postId: {}", post.getId());
    }

    @Override
    public PostResponseDto getPostById(Long id) {
        log.info("게시글 조회 요청 - postId: {}", id);
        
        // 게시글 존재 여부 검증
        Post post = postMapper.findById(id);
        postValidator.validateExistenceFilter(post);
        
        postMapper.increaseViewCount(id);
        log.info("게시글 조회 완료 - postId: {}", id);
        return post;
    }

    @Override
    public void deletePost(Long id, Long userId) {
        log.info("게시글 삭제 요청 - postId: {}, userId: {}", id, userId);
        
        // 계정 존재 여부 검증
        User user = userMapper.findById(userId);
        userValidator.validateExistenceFilter(user);
        
        // 게시글 존재 여부 검증
        Post post = postMapper.findById(id);
        postValidator.validateExistenceFilter(post);
        
        // 작성자 본인 여부 검증
        if (!post.getUserId().equals(userId)) {
            log.warn("게시글 삭제 권한 없음 - postId: {}, userId: {}", id, userId);
            throw new ForbiddenException("게시글 작성자만 삭제할 수 있습니다.");
        }
        
        postMapper.deleteById(id);
        log.info("게시글 삭제 완료 - postId: {}", id);
    }

    @Override
    public List<PostResponseDto> searchPosts(PostSearchConditionDto request) {
        log.info("게시글 목록 조회 요청 - title: {}, nickname: {}", request.getTitle(), request.getNickname());
        
        // 화이트리스트 매핑
        Map<String, String> sortMap = Map.of(
                "createdAt", "created_at",
                "views", "views",
                "title", "title");

        Long userId = null;
        if (request.getNickname() != null && !request.getNickname().isBlank()) {
            userId = userMapper.findByNickname(request.getNickname()).getId();
            request.setUserId(userId);
        }

        String mappedSort = sortMap.getOrDefault(request.getSort(), "created_at");
        request.setSort(mappedSort);
        
        List<PostResponseDto> posts = postMapper.searchPosts(
                request.getTitle(),
                request.getUserId(),
                request.getDateFrom(),
                request.getDateTo(),
                request.getSize(),
                request.getOffset(),
                request.getSort(),
                request.getDirection());
                
        log.info("게시글 목록 조회 완료 - 조회된 게시글 수: {}", posts.size());
        return posts;
    }

    @Override
    public void updatePost(Long id, PostUpdateRequestDto request, Long userId) {
        log.info("게시글 수정 요청 - postId: {}, userId: {}", id, userId);
        
        // 계정 존재 여부 검증
        User user = userMapper.findById(userId);
        userValidator.validateExistenceFilter(user);
        
        // 게시글 존재 여부 검증
        Post post = postMapper.findById(id);
        postValidator.validateExistenceFilter(post);
        
        // 작성자 본인 여부 검증
        if (!post.getUserId().equals(userId)) {
            log.warn("게시글 수정 권한 없음 - postId: {}, userId: {}", id, userId);
            throw new ForbiddenException("게시글 작성자만 수정할 수 있습니다.");
        }
        
        // 수정 사항 반영
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        
        // DB 업데이트
        postMapper.update(post);
        log.info("게시글 수정 완료 - postId: {}", id);
    }
}
