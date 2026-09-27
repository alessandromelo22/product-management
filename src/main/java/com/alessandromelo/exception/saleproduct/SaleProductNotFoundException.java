package com.alessandromelo.exception.saleproduct;

public class SaleProductNotFoundException extends RuntimeException {

    public SaleProductNotFoundException(Long saleProductId) {
        super("SaleProduct with Id: " + saleProductId + " not found!");
    }
}
