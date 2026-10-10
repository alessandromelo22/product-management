package com.alessandromelo.entity;

import com.alessandromelo.enums.ProductCategory;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    @Enumerated(value = EnumType.STRING)
    private ProductCategory productCategory;
    private BigDecimal price; // preço de tabela
    private int stock;

    @OneToMany(mappedBy = "product")
    private List<SaleProduct> saleProducts;

    public Product() {
    }

    public Product(Long id, String name, String brand, ProductCategory productCategory, BigDecimal price, int stock, List<SaleProduct> saleProducts) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.productCategory = productCategory;
        this.price = price;
        this.stock = stock;
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

    public List<SaleProduct> getSaleProducts() {
        return saleProducts;
    }

    public void setSaleProducts(List<SaleProduct> saleProducts) {
        this.saleProducts = saleProducts;
    }
}