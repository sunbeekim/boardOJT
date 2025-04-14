package com.example.board.domain.post.entity.interfaces;

import com.example.board.common.interfaces.EntityBehaviorInterface;

public interface PostBehavior extends EntityBehaviorInterface {

    void update(String title, String content);

    // 제목과 내용의 유효성 다름(패턴, 사이즈 정규식 다르기에 구분)
    void changeTitle(String title);

    void changeContent(String content);

    void increaseView(Long id);

    boolean isDeleted();

    void validateOwnership(Long userId);

    boolean isOwnedBy(Long userId);

}