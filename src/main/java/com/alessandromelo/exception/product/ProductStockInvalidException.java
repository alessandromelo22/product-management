package com.alessandromelo.exception.product;

public class ProductStockInvalidException extends RuntimeException {
    public ProductStockInvalidException() {
        super("Product stock cannot be a negative value!");
    }
}
