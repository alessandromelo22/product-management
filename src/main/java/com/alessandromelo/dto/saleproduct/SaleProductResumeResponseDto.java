package com.alessandromelo.dto.saleproduct;

import java.math.BigDecimal;

public class SaleProductResumeResponseDto {

    private Long productId;
    private Integer quantity; //quantidade de produtos
    private BigDecimal unitPrice; // preço unitário

    public SaleProductResumeResponseDto() {
    }

    public SaleProductResumeResponseDto(Long productId, Integer quantity, BigDecimal unitPrice) {
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
