package com.alessandromelo.entity;

import com.alessandromelo.enums.SaleStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private SaleStatus status;
    private Float totalAmount;
    private Integer installments; // número de parcelas (1 = à vista)
    private Float installmentAmount; // valor de cada parcela
    private LocalDateTime saleDate;

    @ManyToOne
    private Customer customer; //(FK)
    @OneToMany(mappedBy = "sale")
    private List<SaleProduct> saleProducts;


    public Sale() {
    }

    public Sale(Long id, SaleStatus status, Float totalAmount, Integer installments, Float installmentAmount, LocalDateTime saleDate, Customer customer, List<SaleProduct> saleProducts) {
        this.id = id;
        this.status = status;
        this.totalAmount = totalAmount;
        this.installments = installments;
        this.installmentAmount = installmentAmount;
        this.saleDate = saleDate;
        this.customer = customer;
        this.saleProducts = saleProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public Float getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(Float installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public LocalDateTime getPurchaseDate() {
        return saleDate;
    }

    public void setPurchaseDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<SaleProduct> getPurchaseProducts() {
        return saleProducts;
    }

    public void setPurchaseProducts(List<SaleProduct> saleProducts) {
        this.saleProducts = saleProducts;
    }
}