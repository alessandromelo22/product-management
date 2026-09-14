package com.alessandromelo.enums;

public enum ProductCategory {

    FRAGRANCE("Perfume"),
    CREAM("Creme"),
    SOAP("Sabonete"),
    LIPSTICK("Batom"),
    KIT("Kit");

    private final String description;

    ProductCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
