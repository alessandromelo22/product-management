package com.alessandromelo.security.builders;


import com.alessandromelo.security.dto.LoginRequestDto;

public class LoginRequestDtoBuilder {

    private String email;
    private String password;

    public LoginRequestDtoBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public LoginRequestDtoBuilder withPassword(String password){
        this.password = password;
        return this;
    }


    public LoginRequestDto build(){
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail(this.email);
        loginRequestDto.setPassword(this.password);
        return loginRequestDto;
    }

}
