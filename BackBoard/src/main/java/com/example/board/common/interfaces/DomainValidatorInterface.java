package com.example.board.common.interfaces;

// 지금 당장 확장 불필요 추후 작업업
public interface DomainValidatorInterface<T> {
    // 도메인 내 엔티티에서 사용할 검증클래스에서 사용할 공통 인터페이스
    // 예시시
    void validateCreate(T request);

    void validateUpdate(T request);

    void validateDelete(Long id);
}
