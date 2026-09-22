package com.alessandromelo.dto.sale;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class SaleDateRequestDto {
    @NotNull(message = "The start date cannot be null!")
    private LocalDate start;
    @NotNull(message = "The end date cannot be null!")
    private LocalDate end;


    public SaleDateRequestDto() {
    }

    public SaleDateRequestDto(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
