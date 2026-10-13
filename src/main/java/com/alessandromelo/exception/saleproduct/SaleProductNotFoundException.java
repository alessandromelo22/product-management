package com.alessandromelo.exception.saleproduct;

public class SaleProductNotFoundException extends RuntimeException {


    private SaleProductNotFoundException(String message){
        super(message);
    }

    public static SaleProductNotFoundException bySaleProductId(Long saleProductId) {
        return new SaleProductNotFoundException("SaleProduct with Id: " + saleProductId + " not found!");
    }

    public static SaleProductNotFoundException bySaleId(Long saleId) {
        return new SaleProductNotFoundException("Doesn't exists any SaleProduct associate with a Sale with saleId " + saleId + "!");
    }
}
