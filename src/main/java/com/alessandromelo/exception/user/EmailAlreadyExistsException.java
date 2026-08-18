package com.alessandromelo.exception.user;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("Email already registered in the database");
    }
}
