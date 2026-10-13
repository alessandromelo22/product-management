package com.alessandromelo.dto.sale;

import java.time.LocalDate;

public class SalePatchDateRequestDto {

    private LocalDate saleDate;


    public SalePatchDateRequestDto() {
    }

    public SalePatchDateRequestDto(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }
}
