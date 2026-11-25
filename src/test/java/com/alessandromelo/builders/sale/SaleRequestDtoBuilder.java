package com.alessandromelo.builders.sale;

import com.alessandromelo.dto.sale.SaleRequestDto;
import com.alessandromelo.dto.saleproduct.SaleProductResumeRequestDto;
import com.alessandromelo.enums.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class SaleRequestDtoBuilder {
    private List<SaleProductResumeRequestDto> saleProductResumeRequestDtos = List.of(new SaleProductResumeRequestDto(1L, 1, new BigDecimal("50.00")));
    private SaleStatus status = SaleStatus.PAID;
    private Integer installments = 1; // número de parcelas (1 = à vista)
    private LocalDateTime saleDate = LocalDateTime.of(2026, Month.NOVEMBER, 4, 10, 23);
    private Long customerId = 1L;

    public SaleRequestDtoBuilder() {
    }

    public SaleRequestDtoBuilder withSaleProductResumeRequestDtos(List<SaleProductResumeRequestDto> saleProductResumeRequestDtos){
        this.saleProductResumeRequestDtos = saleProductResumeRequestDtos;
        return this;
    }

    public SaleRequestDtoBuilder withStatus(SaleStatus status){
        this.status = status;
        return this;
    }

    public SaleRequestDtoBuilder withInstallments(Integer installments){
        this.installments = installments;
        return this;
    }

    public SaleRequestDtoBuilder withSaleDate(LocalDateTime saleDate){
        this.saleDate = saleDate;
        return this;
    }

    public SaleRequestDtoBuilder withCustomerId(Long customerId){
        this.customerId = customerId;
        return this;
    }

    public SaleRequestDto build (){
        SaleRequestDto requestDto = new SaleRequestDto();
        requestDto.setSaleProductResumeRequestDtos(this.saleProductResumeRequestDtos);
        requestDto.setStatus(this.status);
        requestDto.setInstallments(this.installments);
        requestDto.setSaleDate(this.saleDate);
        requestDto.setCustomerId(this.customerId);
        return requestDto;
    }
}
