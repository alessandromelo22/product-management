package com.alessandromelo.dto.product;

import jakarta.validation.constraints.NotNull;

public class ProductEntryToStockRequestDto {

    @NotNull(message = "The 'inputQuantity' cannot be null")
    private int inputQuantity;
    //pensar talvez em colocar um campo de 'description' pra colocar o que seria essa entrada, ou algo ate um campo de 'entryDate' pra registrar


    public ProductEntryToStockRequestDto() {
    }

    public ProductEntryToStockRequestDto(int inputQuantity) {
        this.inputQuantity = inputQuantity;
    }

    public int getInputQuantity() {
        return inputQuantity;
    }

    public void setInputQuantity(int inputQuantity) {
        this.inputQuantity = inputQuantity;
    }
}
