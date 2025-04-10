package com.example.board.common.dto;

public class JwtUserInfo {
    private final Long id;
    private final String email;
    private final String nickname;

    public JwtUserInfo(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }
}
