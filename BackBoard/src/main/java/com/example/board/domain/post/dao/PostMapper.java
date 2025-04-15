package com.example.board.domain.post.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.board.domain.post.dto.PostResponseDto;
import com.example.board.domain.post.entity.Post;

@Mapper
public interface PostMapper {

        void insert(Post post);

        Post findById(@Param("id") Long id);

        void update(Post post);

        void delete(@Param("id") Long id);

        List<PostResponseDto> searchPosts(
                        @Param("title") String title,
                        @Param("userId") Long userId,
                        @Param("dateFrom") LocalDateTime dateFrom,
                        @Param("dateTo") LocalDateTime dateTo,
                        @Param("size") int size,
                        @Param("offset") int offset,
                        @Param("sort") String sort,
                        @Param("direction") String direction);

        int increaseCommentCount(@Param("id") Long id);

        int increaseViewCount(@Param("id") Long id, @Param("viewCount") int viewCount);

}
