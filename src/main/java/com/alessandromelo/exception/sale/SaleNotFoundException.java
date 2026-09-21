package com.alessandromelo.exception.sale;

public class SaleNotFoundException extends RuntimeException {

    public SaleNotFoundException(Long saleId) {
        super("Sale with id " + saleId + " not found!");
    }
}
