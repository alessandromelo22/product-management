package com.alessandromelo.dto.security;

public class RefreshResponseDto {

    private final String newAccessToken;

    public RefreshResponseDto(String newAccessToken) {
        this.newAccessToken = newAccessToken;
    }

    public String getNewAccessToken() {
        return newAccessToken;
    }
}
