package com.alessandromelo.exception.customer;

public class PhoneNumberAlreadyExistsException extends RuntimeException {
    public PhoneNumberAlreadyExistsException(String phoneNumber) {
        super("phone number: " + phoneNumber + " already registered!");
    }
}
