package com.alessandromelo.exception.product;

public class InvalidStockEntryValueException extends RuntimeException {

    public InvalidStockEntryValueException() {
        super("Invalid stock entry value, the value must be greater than 0!");
    }
}
