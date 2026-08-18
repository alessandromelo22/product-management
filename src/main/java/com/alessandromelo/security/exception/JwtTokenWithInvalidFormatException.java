package com.alessandromelo.security.exception;

public class JwtTokenWithInvalidFormatException extends RuntimeException {
    public JwtTokenWithInvalidFormatException() {
        super("The informed token has a invalid format");
    }
}
