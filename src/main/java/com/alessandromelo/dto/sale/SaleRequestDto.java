package com.alessandromelo.dto.sale;

import com.alessandromelo.dto.saleproduct.SaleProductResumeRequestDto;
import com.alessandromelo.enums.SaleStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SaleRequestDto {

    @Valid
    @NotEmpty
    private List<SaleProductResumeRequestDto> saleProductResumeRequestDtos;

    @NotNull(message = "The Sale 'status' cannot be null")
    private SaleStatus status;
    @NotNull(message = "The Sale 'totalAmount' cannot be null")
    private BigDecimal totalAmount;
    @NotNull(message = "The Sale 'installments' cannot be null")
    private Integer installments; // número de parcelas (1 = à vista)
    @NotNull(message = "The Sale 'installmentAmount' cannot be null")
    private BigDecimal installmentAmount;// valor de cada parcela
    private LocalDateTime saleDate;
    //Nao vou obrigar ser passado um customerId porque as vezes ela vai vender pra algume novo e nao vai lembrar/conseguir pegar os dados da pessoa
    private Long customerId;


    public SaleRequestDto() {
    }

    public SaleRequestDto(List<SaleProductResumeRequestDto> saleProductResumeRequestDtos, SaleStatus status, BigDecimal totalAmount, Integer installments, BigDecimal installmentAmount, LocalDateTime saleDate, Long customerId) {
        this.saleProductResumeRequestDtos = saleProductResumeRequestDtos;
        this.status = status;
        this.totalAmount = totalAmount;
        this.installments = installments;
        this.installmentAmount = installmentAmount;
        this.saleDate = saleDate;
        this.customerId = customerId;
    }

    public List<SaleProductResumeRequestDto> getSaleProductResumeRequestDtos() {
        return saleProductResumeRequestDtos;
    }

    public void setSaleProductResumeRequestDtos(List<SaleProductResumeRequestDto> saleProductResumeRequestDtos) {
        this.saleProductResumeRequestDtos = saleProductResumeRequestDtos;
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
