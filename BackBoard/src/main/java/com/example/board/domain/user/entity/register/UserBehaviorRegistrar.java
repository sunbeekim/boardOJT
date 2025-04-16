package com.example.board.domain.user.entity.register;

import com.example.board.common.factory.BehaviorFactory;
import com.example.board.common.interfaces.BehaviorRegistrarInterface;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.interfaces.UserBehavior;
import com.example.board.domain.user.entity.interfaces.impl.UserBehaviorImpl;

import org.springframework.stereotype.Component;

// 컴포넌트 어노테이션으로 Bean 등록
@Component
public class UserBehaviorRegistrar implements BehaviorRegistrarInterface { // 팩토리를 전달하는 인터페이스 구현

    @Override
    public void register(BehaviorFactory factory) {
        // 팩토리에 엔티티-행동 매핑 등록 (Entity → Behavior 전략 지정)
        factory.registerBehavior(
                User.class,
                UserBehavior.class,
                user -> new UserBehaviorImpl((User) user));
    }
}