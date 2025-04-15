package com.example.board.common.interfaces;

public interface DomainValidatorInterface<T, E> {
    // 도메인 내 엔티티에서 사용할 검증클래스에서 사용할 공통 인터페이스
    // 예시시

    // 생성할때 검증용
    public interface CreateValidator<T, E> {
        void validateCreate(T request, E entity);

        default void validateCreate(T request) {
            validateCreate(request, null);
        }
    }

    // 업데이트할때 검증용
    public interface UpdateValidator<T, E> {
        void validateUpdate(T request, E entity);
    }

    // 삭제할때 검증용
    public interface DeleteValidator<T, E> {
        void validateDelete(T request, E entity);
    }

    // 조회할때 검증용
    public interface SelectValidator<T, E> {
        void validateDelete(T request, E entity);
    }

}
