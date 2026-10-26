package com.alessandromelo.builders.product;

import com.alessandromelo.dto.product.ProductRequestDto;
import com.alessandromelo.enums.ProductCategory;

import java.math.BigDecimal;

public class ProductRequestDtoBuilder {

    private String name = "Creme";
    private String brand = "Oboticário";
    private ProductCategory productCategory = ProductCategory.CREAM;
    private BigDecimal price = new BigDecimal("50.00");
    private int stock = 10;

    public ProductRequestDtoBuilder() {
    }

    public ProductRequestDtoBuilder withName(String name){
        this.name = name;
        return this;
    }

    public ProductRequestDtoBuilder withBrand(String brand){
        this.brand = brand;
        return this;
    }

    public ProductRequestDtoBuilder withProductCategory(ProductCategory productCategory){
        this.productCategory = productCategory;
        return this;
    }

    public ProductRequestDtoBuilder withPrice(BigDecimal price){
        this.price = price;
        return this;
    }

    public ProductRequestDtoBuilder withStock(int stock){
        this.stock = stock;
        return this;
    }

    public ProductRequestDto build(){
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName(this.name);
        requestDto.setBrand(this.brand);
        requestDto.setProductCategory(this.productCategory);
        requestDto.setPrice(this.price);
        requestDto.setStock(this.stock);
        return requestDto;
    }
}
