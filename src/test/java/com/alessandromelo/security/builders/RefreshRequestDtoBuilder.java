package com.alessandromelo.security.builders;

import com.alessandromelo.security.dto.RefreshRequestDto;

public class RefreshRequestDtoBuilder {

    private String refreshToken;


    public RefreshRequestDtoBuilder withRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
        return this;
    }

    public RefreshRequestDto build(){
       return new RefreshRequestDto(this.refreshToken);
    }
}
