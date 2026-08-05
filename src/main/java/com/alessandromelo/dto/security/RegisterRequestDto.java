package com.alessandromelo.dto.security;

import com.alessandromelo.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegisterRequestDto {
    @NotBlank
    private String uName;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private UserRole role;


    public RegisterRequestDto() {
    }

    public RegisterRequestDto(String uName, String email, String password, UserRole role) {
        this.uName = uName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }


    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
