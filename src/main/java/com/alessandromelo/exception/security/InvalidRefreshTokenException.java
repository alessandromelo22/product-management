package com.alessandromelo.exception.security;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("Invalid refreshToken!");
    }
}
