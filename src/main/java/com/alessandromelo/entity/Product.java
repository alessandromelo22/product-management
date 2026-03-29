package com.alessandromelo.entity;

import com.alessandromelo.enums.ProductCategory;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private ProductCategory productCategory;
    private Float price;

    @OneToMany(mappedBy = "product")
    private List<SaleProduct> saleProducts;

    public Product() {
    }

    public Product(Long id, String name, ProductCategory productCategory, Float price, List<SaleProduct> saleProducts) {
        this.id = id;
        this.name = name;
        this.productCategory = productCategory;
        this.price = price;
        this.saleProducts = saleProducts;
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

    public ProductCategory getProductType() {
        return productCategory;
    }

    public void setProductType(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public List<SaleProduct> getPurchaseProducts() {
        return saleProducts;
    }

    public void setPurchaseProducts(List<SaleProduct> saleProducts) {
        this.saleProducts = saleProducts;
    }
}