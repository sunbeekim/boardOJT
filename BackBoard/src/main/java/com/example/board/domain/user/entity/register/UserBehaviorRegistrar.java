package com.example.board.domain.user.entity.register;

import com.example.board.common.factory.BehaviorFactory;
import com.example.board.common.interfaces.BehaviorRegistrarInterface;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.interfaces.UserBehavior;
import com.example.board.domain.user.entity.interfaces.impl.UserBehaviorImpl;

import org.springframework.stereotype.Component;

// BehaviorFactory의 실제 생성자는 List<BehaviorRegistrar> registrars 를 인자값으로 받음
// 컴포넌트 어노테이션으로 빈에 등록하면 스프링이 DI(의존성 주입)을 알아서 처리
// 따라서, 빈에 등록되는 객체에서 다른 빈을 생성자 인자로 요구해도 직접 전달하지 않아도 됨
@Component
public class UserBehaviorRegistrar implements BehaviorRegistrarInterface {

    @Override
    public void register(BehaviorFactory factory) {
        // BehaviorFactory가 제공하는 메서드를 통해,
        // 특정 엔티티(User.class)에 대한 동작(Behavior)을 등록
        //
        // 첫 번째 인자: 실제 엔티티 클래스 (User.class)
        // 두 번째 인자: 해당 엔티티에 대응하는 Behavior 인터페이스 (UserBehavior.class)
        // 세 번째 인자: User 객체를 받아 UserBehaviorImpl 인스턴스를 생성하는 람다 (생성 로직)
        //
        // 이렇게 등록하면, BehaviorFactory에서 UserBehavior.class 타입을 요청할 때
        // UserBehaviorImpl이 자동으로 생성되어 반환됨
        factory.registerBehavior(
                User.class,
                UserBehavior.class,
                user -> new UserBehaviorImpl((User) user));
    }
}