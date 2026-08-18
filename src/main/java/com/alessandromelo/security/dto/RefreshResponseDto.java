package com.alessandromelo.security.dto;

public class RefreshResponseDto {

    private final String newAccessToken;

    public RefreshResponseDto(String newAccessToken) {
        this.newAccessToken = newAccessToken;
    }

    public String getNewAccessToken() {
        return newAccessToken;
    }
}
