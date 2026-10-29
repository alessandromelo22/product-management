package com.alessandromelo.exception.product;

public class StockEntryLimitExceededException extends RuntimeException {
    public StockEntryLimitExceededException() {
        super("The value of items entering stock cannot exceed 50!");
    }
}
