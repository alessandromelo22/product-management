package com.alessandromelo.builders.product;

import com.alessandromelo.dto.product.ProductResponseDto;
import com.alessandromelo.enums.ProductCategory;

import java.math.BigDecimal;

public class ProductResponseDtoBuilder {

    private Long id = 1L;
    private String name = "Creme";
    private String brand = "Oboticário";
    private ProductCategory productCategory = ProductCategory.CREAM;
    private BigDecimal price = new BigDecimal("50.00");
    private int stock = 10;

    public ProductResponseDtoBuilder() {
    }

    public ProductResponseDtoBuilder withId(Long id){
        this.id = id;
        return this;
    }

    public ProductResponseDtoBuilder withName(String name){
        this.name = name;
        return this;
    }

    public ProductResponseDtoBuilder withBrand(String brand){
        this.brand = brand;
        return this;
    }

    public ProductResponseDtoBuilder withProductCategory(ProductCategory productCategory){
        this.productCategory = productCategory;
        return this;
    }

    public ProductResponseDtoBuilder withPrice(BigDecimal price){
        this.price = price;
        return this;
    }

    public ProductResponseDtoBuilder withStock(int stock){
        this.stock = stock;
        return this;
    }

    public ProductResponseDto build(){
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(this.id);
        responseDto.setName(this.name);
        responseDto.setBrand(this.brand);
        responseDto.setProductCategory(this.productCategory);
        responseDto.setPrice(this.price);
        responseDto.setStock(this.stock);
        return responseDto;
    }
}
