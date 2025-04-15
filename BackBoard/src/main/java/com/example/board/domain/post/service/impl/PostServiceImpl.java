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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostValidator postValidator;
    private final UserMapper userMapper;

    @Override
    public void createPost(PostCreateRequestDto request, Long userId) {
        // 엔티티와 요청데이터 매핑
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .userId(userId)
                .build();
        // 유효성 검사는 dto에서 처리
        // 작성하는 사용자 id 검증
        postValidator.validateCreate(post, userId);
        // post.postBehavior().isOwnedBy(userId);

        // db 저장
        postMapper.insert(post);
    }

    @Override
    public PostResponseDto getPostById(Long id) {
        Post post = postMapper.findById(id);
        log.info("post ={}", post);
        postMapper.increaseViewCount(id);
        return PostMapper.toResponseDto(post);
    }

    @Override
    public void deletePost(Long id, Long userId) {
        Post post = postMapper.findById(id);
        post.postBehavior().validateOwnership(userId);

    }

    @Override
    public List<PostResponseDto> searchPosts(PostSearchConditionDto request) {
        // 화이트리스트 매핑
        Map<String, String> sortMap = Map.of(
                "createdAt", "created_at",
                "views", "views",
                "title", "title");

        Long userId;
        if (request.getNickname() != null && !request.getNickname().isBlank()) {
            userId = userMapper.findByNickname(request.getNickname()).getId();
            request.setUserId(userId);
        }

        String mappedSort = sortMap.getOrDefault(request.getSort(), "created_at");
        request.setSort(mappedSort);
        return postMapper.searchPosts(
                request.getTitle(),
                request.getUserId(),
                request.getDateFrom(),
                request.getDateTo(),
                request.getSize(),
                request.getOffset(),
                request.getSort(),
                request.getDirection());
    }

    @Override
    public void updatePost(Long id, PostUpdateRequestDto request, Long userId) {
        Post post = postMapper.findById(id);

        // 소유자 검증
        post.postBehavior().validateOwnership(userId);

        // 수정 사항 반영
        post.postBehavior().update(request.getTitle(), request.getContent());

        // DB 업데이트
        postMapper.update(post);
    }

}
