package com.alessandromelo.builders.sale;

import com.alessandromelo.entity.Customer;
import com.alessandromelo.entity.Sale;
import com.alessandromelo.entity.SaleProduct;
import com.alessandromelo.enums.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class SaleBuilder {
    private Long id = 1L;
    private SaleStatus status = SaleStatus.PAID;
    private BigDecimal totalAmount = new BigDecimal("200.00"); //valor total da compra
    private Integer installments = 1; // número de parcelas (1 = à vista)
    private BigDecimal installmentAmount = new BigDecimal("200.00"); // valor de cada parcela
    private LocalDateTime saleDate = LocalDateTime.of(2026, Month.NOVEMBER, 4, 10, 23);;
    private Customer customer = new Customer(); //(FK)
    private List<SaleProduct> saleProducts = List.of(new SaleProduct());

    public SaleBuilder (){

    }

    public SaleBuilder withId(Long id){
        this.id = id;
        return this;
    }

    public SaleBuilder withStatus(SaleStatus status){
        this.status = status;
        return this;
    }

    public SaleBuilder withTotalAmount(BigDecimal totalAmount){
        this.totalAmount = totalAmount;
        return this;
    }

    public SaleBuilder withInstallments(Integer installments){
        this.installments = installments;
        return this;
    }

    public SaleBuilder withInstallmentAmount(BigDecimal installmentAmount){
        this.installmentAmount = installmentAmount;
        return this;
    }

    public SaleBuilder withSaleDate(LocalDateTime saleDate){
        this.saleDate = saleDate;
        return this;
    }

    public SaleBuilder withCustomer(Customer customer){
        this.customer = customer;
        return this;
    }

    public SaleBuilder withSaleProducts(List<SaleProduct> saleProducts){
        this.saleProducts = saleProducts;
        return this;
    }

    public Sale build(){
        Sale sale = new Sale();
        sale.setId(this.id);
        sale.setStatus(this.status);
        sale.setTotalAmount(this.totalAmount);
        sale.setInstallments(this.installments);
        sale.setInstallmentAmount(this.installmentAmount);
        sale.setSaleDate(this.saleDate);
        sale.setCustomer(this.customer);
        sale.setSaleProducts(this.saleProducts);
        return sale;
    }
}
