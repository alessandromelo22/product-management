package com.alessandromelo.dto.saleproduct;

import java.math.BigDecimal;

public class SaleProductResponseDto {

    private Long id;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Long saleId; //FK
    private Long productId; //FK

    public SaleProductResponseDto() {
    }

    public SaleProductResponseDto(Long id, Integer quantity, BigDecimal unitPrice, Long saleId, Long productId) {
        this.id = id;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.saleId = saleId;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
