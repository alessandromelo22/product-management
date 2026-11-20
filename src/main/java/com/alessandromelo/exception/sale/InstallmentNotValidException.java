package com.alessandromelo.exception.sale;

public class InstallmentNotValidException extends RuntimeException {
    public InstallmentNotValidException() {
        super("Installment must be greater than 0");
    }
}

