package com.alessandromelo.dto.sale;

import com.alessandromelo.enums.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SaleResponseDto {

    private Long id;
    private SaleStatus status;
    private BigDecimal totalAmount;
    private Integer installments; //número de parcelas (1 = à vista)
    private BigDecimal installmentAmount; //valor de cada parcela
    private LocalDateTime saleDate;
    private Long customerId;

    public SaleResponseDto() {
    }

    public SaleResponseDto(Long id, SaleStatus status, BigDecimal totalAmount, Integer installments, BigDecimal installmentAmount, LocalDateTime saleDate, Long customerId) {
        this.id = id;
        this.status = status;
        this.totalAmount = totalAmount;
        this.installments = installments;
        this.installmentAmount = installmentAmount;
        this.saleDate = saleDate;
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public BigDecimal getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(BigDecimal installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
