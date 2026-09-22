package com.alessandromelo.dto.product;

import com.alessandromelo.enums.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProductRequestDto {

    @NotBlank(message = "The Product 'name' cannot be left blank")
    private String name;
    @NotBlank(message = "The Product 'brand' cannot be left blank")
    private String brand;
    @NotNull(message = "The 'productCategory' cannot be null")
    private ProductCategory productCategory;
    @NotNull(message = "The Product 'price' cannot be left blank")
    private BigDecimal price;

    public ProductRequestDto() {
    }

    public ProductRequestDto(String name, String brand, ProductCategory productCategory, BigDecimal price) {
        this.name = name;
        this.brand = brand;
        this.productCategory = productCategory;
        this.price = price;
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
}
