package com.example.board.common.interfaces;

public interface EntityBehavior {
    default Long getId() {
        return null;
    }

    default void markAsModified() {

    }

    default boolean isDeletable() {
        return false;
    }

    default void softDelete() {
    }
}