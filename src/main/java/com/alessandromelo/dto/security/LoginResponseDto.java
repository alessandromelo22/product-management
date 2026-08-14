package com.alessandromelo.dto.security;

public class LoginResponseDto {

    private final String accessToken; //token que sera mandado nas requisições
    private final String refreshToken; //token usado para criar outros accessTokens

    public LoginResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
