package com.board.board.dao;

import com.board.board.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByEmail(@Param("email") String email);
    User findByNickname(@Param("nickname") String nickname);
    void save(User user);
    void update(User user);
    void updateLoginFailCount(@Param("id") Long id, @Param("loginFailCount") int loginFailCount, @Param("locked") boolean locked);
    void delete(@Param("id") Long id);
} 