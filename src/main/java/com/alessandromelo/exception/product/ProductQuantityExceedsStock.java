package com.alessandromelo.exception.product;

public class ProductQuantityExceedsStock extends RuntimeException {

    public ProductQuantityExceedsStock(String productName, int stock) {
        super("The sale cannot be completed because product '" + productName + "' has " + stock + " units in stock.");
    }
}
