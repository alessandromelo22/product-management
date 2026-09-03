package com.alessandromelo.security.builders;

import com.alessandromelo.enums.UserRole;
import com.alessandromelo.security.dto.RegisterRequestDto;

public class RegisterRequestDtoBuilder {

    private String uName = "Maria";
    private String email = "mariazinha244@gmail.com";
    private String password = "123456";
    private UserRole role = UserRole.USER;


    public RegisterRequestDtoBuilder() {
    }

    public RegisterRequestDtoBuilder(String uName, String email, String password, UserRole role) {
        this.uName = uName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public RegisterRequestDtoBuilder withUName(String uName){
        this.uName = uName;
        return this;
    }

    public RegisterRequestDtoBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public RegisterRequestDtoBuilder withPassword(String password){
        this.password = password;
        return this;
    }

    public RegisterRequestDtoBuilder withRole(UserRole role){
        this.role = role;
        return this;
    }


    public RegisterRequestDto build(){
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setuName(this.uName);
        registerRequestDto.setEmail(this.email);
        registerRequestDto.setPassword(this.password);
        registerRequestDto.setRole(this.role);

        return  registerRequestDto;
    }
}
