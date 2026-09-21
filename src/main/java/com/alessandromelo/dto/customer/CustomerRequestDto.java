package com.alessandromelo.dto.customer;

import jakarta.validation.constraints.NotBlank;

public class CustomerRequestDto {
    @NotBlank(message = "The Customer 'name' cannot be left blank")
    private String name;
    @NotBlank(message = "The Customer 'phoneNumber' cannot be left blank")
    private String phoneNumber;
    @NotBlank(message = "The Customer 'cpf' cannot be left blank")
    //Colocar a annotation @CPF e alterar os testes
    private String cpf;

    public CustomerRequestDto(String name, String phoneNumber, String cpf) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.cpf = cpf;
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
}
