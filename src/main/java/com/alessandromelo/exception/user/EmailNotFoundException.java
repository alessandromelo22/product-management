package com.alessandromelo.exception.user;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String email) {
        super("Email: " + email + " not found!");
    }
}
