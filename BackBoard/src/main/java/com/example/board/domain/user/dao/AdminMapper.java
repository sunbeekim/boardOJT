package com.example.board.domain.user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.board.domain.user.entity.User;

@Mapper
public interface AdminMapper {
    void postDelete(Long id);

    void commentDelete(Long id);

    void userDelete(Long id);

    void unlocked(User user);

}
