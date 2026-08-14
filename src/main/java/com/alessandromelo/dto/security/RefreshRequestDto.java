package com.alessandromelo.dto.security;

public class RefreshRequestDto {

    private final String refreshToken;

    public RefreshRequestDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    public String getRefreshToken() {
        return refreshToken;
    }
}
