package com.alessandromelo.security.controller;

import com.alessandromelo.security.dto.*;
import com.alessandromelo.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Operations focused on security.")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @Operation(
            summary = "Registers the user in the application.",
            description = "It allows the user to register for the application, thereby enabling them to log in and use the features.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registration successful!"),
            @ApiResponse(responseCode = "400", description = "Request with a null or empty field!"),
            @ApiResponse(responseCode = "401", description = "Email already registered in the database!"),
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequestDto registerRequestDto){

        this.authService.register(registerRequestDto);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "Logs the user in.",
            description = "Allows the user to log in to the application after having already registered in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful!"),
            @ApiResponse(responseCode = "400", description = "Request with a null or empty field"),
            @ApiResponse(responseCode = "401", description = "Login failed!")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(this.authService.login(loginRequestDto));
    }


    @Operation(
            summary = "Generates a new accessToken",
            description = "The `refreshToken` allows for the creation of a new `accessToken`, enabling the user to continue accessing resources without needing to log in." +
                    " If the `refreshToken` is invalid, the user is required to log in.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "New accessToken successfully generated!"),
            @ApiResponse(responseCode = "400", description = "Request with a null or empty field!"),
            @ApiResponse(responseCode = "401", description = "Invalid refreshToken!"),
    })
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refresh(@RequestBody @Valid RefreshRequestDto refreshRequestDto){
        return ResponseEntity.ok(this.authService.refresh(refreshRequestDto));
    }
}
