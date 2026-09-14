package com.alessandromelo.entity;

import com.alessandromelo.enums.SaleStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private SaleStatus status;
    private BigDecimal totalAmount;
    private Integer installments; // número de parcelas (1 = à vista)
    private BigDecimal installmentAmount; // valor de cada parcela
    private LocalDateTime saleDate;

    @ManyToOne
    private Customer customer; //(FK)
    @OneToMany(mappedBy = "sale")
    private List<SaleProduct> saleProducts;


    public Sale() {
    }

    public Sale(Long id, SaleStatus status, BigDecimal totalAmount, Integer installments, BigDecimal installmentAmount, LocalDateTime saleDate, Customer customer, List<SaleProduct> saleProducts) {
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public BigDecimal getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(BigDecimal installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<SaleProduct> getSaleProducts() {
        return saleProducts;
    }

    public void setSaleProducts(List<SaleProduct> saleProducts) {
        this.saleProducts = saleProducts;
    }
}