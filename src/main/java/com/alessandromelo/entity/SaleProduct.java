package com.alessandromelo.entity;


import jakarta.persistence.*;

@Entity
public class SaleProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    private Float unitPrice;

    @ManyToOne
    private Sale sale; //FK
    @ManyToOne
    private Product product; //FK


    public SaleProduct() {
    }

    public SaleProduct(Long id, Integer quantity, Float unitPrice, Sale sale, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.sale = sale;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Sale getPurchase() {
        return sale;
    }

    public void setPurchase(Sale sale) {
        this.sale = sale;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
