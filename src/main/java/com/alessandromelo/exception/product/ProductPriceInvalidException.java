package com.alessandromelo.exception.product;

public class ProductPriceInvalidException extends RuntimeException {
    public ProductPriceInvalidException() {
        super("The product 'price' must be a value greater than 0!");
    }
}
