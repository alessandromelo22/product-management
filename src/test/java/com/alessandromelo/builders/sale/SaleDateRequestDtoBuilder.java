package com.alessandromelo.builders.sale;

import com.alessandromelo.dto.sale.SaleDateRequestDto;

import java.time.LocalDate;
import java.time.Month;

public class SaleDateRequestDtoBuilder {

    private LocalDate start = LocalDate.of(2026, Month.NOVEMBER, 1);
    private LocalDate end = LocalDate.of(2026, Month.NOVEMBER, 4);

    public SaleDateRequestDtoBuilder() {
    }

    public SaleDateRequestDtoBuilder withStart(LocalDate start){
        this.start = start;
        return this;
    }

    public SaleDateRequestDtoBuilder withEnd(LocalDate end){
        this.end = end;
        return this;
    }

    public SaleDateRequestDto build() {
        SaleDateRequestDto requestDto = new SaleDateRequestDto();
        requestDto.setStart(this.start);
        requestDto.setEnd(this.end);

        return requestDto;
    }
}
