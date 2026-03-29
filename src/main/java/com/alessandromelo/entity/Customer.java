package com.alessandromelo.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phoneNumber;
    private String cpf;

    @OneToMany(mappedBy = "customer")
    private List<Sale> sales;

    public Customer() {
    }

    public Customer(Long id, String name, String phoneNumber, String cpf, List<Sale> sales) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.cpf = cpf;
        this.sales = sales;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<Sale> getPurchases() {
        return sales;
    }

    public void setPurchases(List<Sale> sales) {
        this.sales = sales;
    }
}