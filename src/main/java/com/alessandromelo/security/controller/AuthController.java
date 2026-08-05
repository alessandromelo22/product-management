package com.alessandromelo.security.controller;

import com.alessandromelo.dto.security.LoginRequestDto;
import com.alessandromelo.dto.security.LoginResponseDto;
import com.alessandromelo.dto.security.RegisterRequestDto;
import com.alessandromelo.security.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {

        return ResponseEntity.ok(this.authService.login(loginRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequestDto registerRequestDto){

        this.authService.register(registerRequestDto);
        return ResponseEntity.ok().build();
    }
}
