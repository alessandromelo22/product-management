package com.alessandromelo.builders.customer;

import com.alessandromelo.entity.Customer;

public class CustomerBuilder {

    private Long id = 1L;
    private String name = "João da Silva";
    private String phoneNumber = "37123456789";
    private String cpf = "11111111111";


    public CustomerBuilder withId(Long id){
        this.id = id;
        return this;
    }

    public CustomerBuilder withName(String name){
        this.name = name;
        return this;
    }

    public CustomerBuilder withPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
        return this;
    }

    public CustomerBuilder withCpf(String cpf){
        this.cpf = cpf;
        return this;
    }


    public Customer build(){
        Customer customer = new Customer();
        customer.setId(this.id);
        customer.setName(this.name);
        customer.setPhoneNumber(this.phoneNumber);
        customer.setCpf(this.cpf);
        return customer;
    }
}
