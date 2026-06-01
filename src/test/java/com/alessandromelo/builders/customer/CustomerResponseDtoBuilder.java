package com.alessandromelo.builders.customer;

import com.alessandromelo.dto.customer.CustomerResponseDto;

public class CustomerResponseDtoBuilder {

    private Long id = 1L;
    private String name = "João da Silva";
    private String phoneNumber = "37123456789";

    public CustomerResponseDtoBuilder withId(Long id){
        this.id = id;
        return this;
    }

    public CustomerResponseDtoBuilder withName(String name){
        this.name = name;
        return this;
    }

    public CustomerResponseDtoBuilder withPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
        return this;
    }

    public CustomerResponseDto build(){
        return new CustomerResponseDto(
                this.id,
                this.name,
                this.phoneNumber
        );
    }

}
