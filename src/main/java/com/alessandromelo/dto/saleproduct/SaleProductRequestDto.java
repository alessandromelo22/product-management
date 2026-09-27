package com.alessandromelo.dto.saleproduct;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class SaleProductRequestDto {

    @NotNull(message = "The SaleProduct 'quantity' cannot be null")
    private Integer quantity;
    @NotNull(message = "The SaleProduct 'unitPrice' cannot be null")
    private BigDecimal unitPrice;
    @NotNull(message = "The SaleProduct 'saleId' cannot be null")
    private Long saleId; //FK
    @NotNull(message = "The SaleProduct 'productId' cannot be null")
    private Long productId; //FK

    public SaleProductRequestDto() {
    }

    public SaleProductRequestDto(Integer quantity, BigDecimal unitPrice, Long saleId, Long productId) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.saleId = saleId;
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
