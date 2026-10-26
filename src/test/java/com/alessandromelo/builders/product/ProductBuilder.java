package com.alessandromelo.builders.product;

import com.alessandromelo.entity.Product;
import com.alessandromelo.enums.ProductCategory;

import java.math.BigDecimal;

public class ProductBuilder {

    private Long id = 1L;
    private String name = "Creme";
    private String brand = "Oboticário";
    private ProductCategory productCategory = ProductCategory.CREAM;
    private BigDecimal price = new BigDecimal("50.00");
    private int stock = 10;

    public ProductBuilder() {
    }

    public ProductBuilder withId(Long id){
        this.id = id;
        return this;
    }

    public ProductBuilder withName(String name){
        this.name = name;
        return this;
    }

    public ProductBuilder withBrand(String brand){
        this.brand = brand;
        return this;
    }

    public ProductBuilder withProductCategory(ProductCategory productCategory){
        this.productCategory = productCategory;
        return this;
    }

    public ProductBuilder withPrice(BigDecimal price){
        this.price = price;
        return this;
    }

    public ProductBuilder withStock(int stock){
        this.stock = stock;
        return this;
    }

    public Product build(){
        Product product = new Product();
        product.setId(this.id);
        product.setName(this.name);
        product.setBrand(this.brand);
        product.setProductCategory(this.productCategory);
        product.setPrice(this.price);
        product.setStock(this.stock);
        return product;
    }
}
