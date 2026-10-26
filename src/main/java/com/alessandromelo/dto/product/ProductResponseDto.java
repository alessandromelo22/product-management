package com.alessandromelo.dto.product;

import com.alessandromelo.enums.ProductCategory;

import java.math.BigDecimal;

public class ProductResponseDto {

    private Long id;
    private String name;
    private String brand;
    private ProductCategory productCategory;
    private BigDecimal price;
    private int stock;

    public ProductResponseDto() {
    }

    public ProductResponseDto(Long id, String name, String brand, ProductCategory productCategory, BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.productCategory = productCategory;
        this.price = price;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
