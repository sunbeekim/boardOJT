package com.example.board.domain.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.board.domain.user.entity.User;


@Mapper
public interface UserMapper {
    User findById(@Param("id") Object id);

    User findByEmail(@Param("email") String email);

    User findByNickname(@Param("nickname") String nickname);

    String findNicknameById(@Param("id") Long id);

    void save(User user);

    void update(User user);

    void updateLoginFailCount(@Param("id") Long id, @Param("loginFailCount") int loginFailCount,
            @Param("locked") boolean locked);

    void delete(@Param("id") Long id);

 
}