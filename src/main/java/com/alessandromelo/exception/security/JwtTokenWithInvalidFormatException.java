package com.alessandromelo.exception.security;

public class JwtTokenWithInvalidFormatException extends RuntimeException {
    public JwtTokenWithInvalidFormatException() {
        super("The informed token has a invalid format");
    }
}
