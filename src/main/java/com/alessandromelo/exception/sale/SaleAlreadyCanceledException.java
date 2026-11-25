package com.alessandromelo.exception.sale;

public class SaleAlreadyCanceledException extends RuntimeException {
    public SaleAlreadyCanceledException(Long saleId) {
        super("Sale with id " + saleId + " already canceled!");
    }
}
