package com.alessandromelo.security.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshRequestDto {

    @NotBlank(message = "refreshToken must be provided!")
    private final String refreshToken;

    public RefreshRequestDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    public String getRefreshToken() {
        return refreshToken;
    }
}
