package com.alessandromelo.dto.saleproduct;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class SaleProductResumeRequestDto {

    @NotNull(message = "The Sale 'productId' cannot be null")
    private Long productId;
    @NotNull(message = "The Sale 'quantity' cannot be null")
    private Integer quantity; //quantidade de produtos
    @NotNull(message = "The Sale 'unitPrice' cannot be null")
    private BigDecimal unitPrice; // preço unitário

    public SaleProductResumeRequestDto() {
    }

    public SaleProductResumeRequestDto(Long productId, Integer quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
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
}
