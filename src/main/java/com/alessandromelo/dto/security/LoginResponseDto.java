package com.alessandromelo.dto.security;

public class LoginResponseDto {

    private final String jwtToken;

    public LoginResponseDto(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
