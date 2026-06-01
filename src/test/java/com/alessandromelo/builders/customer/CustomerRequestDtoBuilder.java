package com.alessandromelo.builders.customer;

import com.alessandromelo.dto.customer.CustomerRequestDto;

public class CustomerRequestDtoBuilder {

    private String name = "João da Silva";
    private String phoneNumber = "37123456789";
    private String cpf = "11111111111";

    public CustomerRequestDtoBuilder withName(String name){
        this.name = name;
        return this;
    }

    public CustomerRequestDtoBuilder withPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
        return this;
    }

    public CustomerRequestDtoBuilder withCpf(String cpf){
        this.cpf = cpf;
        return this;
    }


    public CustomerRequestDto build(){
        return new CustomerRequestDto(
                this.name,
                this.phoneNumber,
                this.cpf
        );
    }
}
