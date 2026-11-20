package com.alessandromelo.builders.sale;

import com.alessandromelo.dto.sale.SaleResponseDto;
import com.alessandromelo.dto.saleproduct.SaleProductResumeResponseDto;
import com.alessandromelo.enums.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SaleResponseDtoBuilder {

    private Long id = 1L;
    private List<SaleProductResumeResponseDto> saleProductResumeResponseDtos = List.of(new SaleProductResumeResponseDto());
    private SaleStatus status = SaleStatus.PAID;
    private BigDecimal totalAmount = new BigDecimal("200.00"); //valor total da compra
    private Integer installments = 1; // número de parcelas (1 = à vista)
    private BigDecimal installmentAmount = new BigDecimal("200.00"); // valor de cada parcela
    private LocalDateTime saleDate = LocalDateTime.now();
    private Long customerId = 1L; //(FK)

    public SaleResponseDtoBuilder() {
    }

    public SaleResponseDtoBuilder withId(Long id){
        this.id = id;
        return this;
    }

    public SaleResponseDtoBuilder withSaleProductResumeResponseDtos(List<SaleProductResumeResponseDto> saleProductResumeResponseDtos){
        this.saleProductResumeResponseDtos = saleProductResumeResponseDtos;
        return this;
    }

    public SaleResponseDtoBuilder withStatus(SaleStatus status){
        this.status = status;
        return this;
    }

    public SaleResponseDtoBuilder withTotalAmount(BigDecimal totalAmount){
        this.totalAmount = totalAmount;
        return this;
    }

    public SaleResponseDtoBuilder withInstallments(Integer installments){
        this.installments = installments;
        return this;
    }

    public SaleResponseDtoBuilder withInstallmentAmount(BigDecimal installmentAmount){
        this.installmentAmount = installmentAmount;
        return this;
    }

    public SaleResponseDtoBuilder withSaleDate(LocalDateTime saleDate){
        this.saleDate = saleDate;
        return this;
    }

    public SaleResponseDtoBuilder withCustomerId(Long customerId){
        this.customerId = customerId;
        return this;
    }

    public SaleResponseDto build(){
        SaleResponseDto saleResponseDto = new SaleResponseDto();
        saleResponseDto.setId(this.id);
        saleResponseDto.setSaleProductResumeResponseDtos(this.saleProductResumeResponseDtos);
        saleResponseDto.setStatus(this.status);
        saleResponseDto.setTotalAmount(this.totalAmount);
        saleResponseDto.setInstallments(this.installments);
        saleResponseDto.setInstallmentAmount(this.installmentAmount);
        saleResponseDto.setSaleDate(this.saleDate);
        saleResponseDto.setCustomerId(this.customerId);
        return saleResponseDto;
    }
}
