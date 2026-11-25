package com.alessandromelo.exception.saleproduct;

public class QuantityNotValidException extends RuntimeException {
    public QuantityNotValidException() {
        super("Quantity must be greater than 0!");
    }
}
