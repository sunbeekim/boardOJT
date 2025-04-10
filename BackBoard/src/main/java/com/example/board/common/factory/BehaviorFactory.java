package com.example.board.common.factory;

import com.example.board.common.interfaces.BehaviorRegistrar;
import com.example.board.common.interfaces.EntityBehavior;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class BehaviorFactory {
    // behaviorMap은 Behavior 인터페이스 타입(Class)을 key로,
    // 해당 타입의 구현체를 생성하는 Function을 value로 저장하는 맵
    // → 어떤 엔티티 타입에 대해 어떤 행동(Behavior) 구현을 생성할지 매핑
    private final Map<Class<?>, Function<Object, EntityBehavior>> behaviorMap;

    // 생성자에서 BehaviorRegistrar들을 주입받아,
    // 각각의 registrar가 자신이 등록할 behavior를 factory에 등록하도록 위임
    public BehaviorFactory(List<BehaviorRegistrar> registrars) {
        this.behaviorMap = new HashMap<>();
        // 각 BehaviorRegistrar가 자신의 behavior를 이 factory에 등록
        registrars.forEach(registrar -> registrar.register(this));
    }

    // behaviorMap에 새로운 behavior 생성 로직을 등록
    // entityType: 실제 엔티티 클래스 (예: User.class)
    // behaviorType: 해당 엔티티에 연결된 Behavior 인터페이스 타입 (예: UserBehavior.class)
    // creator: 엔티티 객체를 받아 behavior 구현체를 생성하는 람다 함수
    // 제네릭 메서드 - 부모의 하위 타입만 받을 수 있게 명시
    // ex) EntityBehavior 이거나, 이를 상속받는 인터페이스를 구현한 클래스만 B에 전달될 수있음
    // 빈에 등록하기 위한 메서드일 뿐 실제 사용되는 구현체가 아님
    public <E, B extends EntityBehavior> void registerBehavior(
            Class<E> entityType,
            Class<B> behaviorType,
            Function<E, B> creator) {

        // behaviorType을 key로, creator 함수를 저장
        // creator는 Object → EntityBehavior로 캐스팅 가능한 함수로 변환하여 저장
        behaviorMap.put(behaviorType, entity -> creator.apply(entityType.cast(entity)));
    }

    // 외부에서 요청한 Behavior 인터페이스 타입에 따라
    // 해당 구현체를 생성하여 반환
    // entity: 실제 엔티티 객체
    // type: 요청한 Behavior 인터페이스 클래스
    // registerBehavior 메서드를 통해 등록된 타입별 구현체를 사용하기 위한 메서드드
    public <T extends EntityBehavior> T wrap(Object entity, Class<T> type) {
        // behaviorMap에서 해당 타입의 생성 함수를 조회
        Function<Object, EntityBehavior> creator = behaviorMap.get(type);
        if (creator == null) {
            // 등록되지 않은 타입이면 예외 발생
            throw new IllegalArgumentException(
                    String.format("Behavior type %s is not registered", type.getName()));
        }
        // 해당 생성 함수를 실행하여 구현체 반환
        return type.cast(creator.apply(entity));
    }
}
