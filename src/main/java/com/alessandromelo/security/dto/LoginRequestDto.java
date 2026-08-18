package com.alessandromelo.security.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDto {
    @NotBlank(message = "The email address must be provided!")
    private String email;
    @NotBlank(message = "The password must be provided!")
    private String password;

    public LoginRequestDto(){
    }

    public LoginRequestDto(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
