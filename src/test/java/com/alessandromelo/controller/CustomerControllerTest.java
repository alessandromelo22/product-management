package com.alessandromelo.controller;


import com.alessandromelo.dto.customer.CustomerRequestDto;
import com.alessandromelo.entity.Customer;
import com.alessandromelo.entity.Sale;
import com.alessandromelo.enums.SaleStatus;
import com.alessandromelo.repository.CustomerRepository;
import com.alessandromelo.repository.SaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper; //serialization and deserialization
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SaleRepository saleRepository;


//getAll()
    @Test
    void getAllShouldReturnStatus200Successfully() throws Exception {
        //Arrange
        this.customerRepository.save(new Customer(null,"João", "34887965546", "067.854.802-44", List.of()));
        this.customerRepository.save(new Customer(null,"Maria", "37866546743", "125.367.219-00", List.of()));

        //Act
        //Assert
        this.mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAllShouldReturnStatus200WhenTheListIsEmpty() throws Exception {
        //Act
        //Assert
        this.mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

//getById:
    @Test
    void getByIdShouldReturnStatus200Successfully() throws Exception {
        //Arrange
        Customer customer = this.customerRepository.save(new Customer(null,"João", "34887965546", "067.854.802-44", List.of()));

        //Act
        //Assert
        this.mockMvc.perform(get("/customers/{customerId}", customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João"));

    }

    @Test
    void getByIdShouldReturnStatus404WhenTheCustomerIsNotFound() throws Exception {
        //Act
        //Assert
        this.mockMvc.perform(get("/customers/{customerId}", 999L))
                .andExpect(status().isNotFound());
    }

//create:
    @Test
    void createShouldReturnStatus201Sucessfully() throws Exception {
        //Arrange:
        CustomerRequestDto requestDto = new CustomerRequestDto("Maria", "34988760098", "976.566.435-67");

        this.mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Maria"))
                .andExpect(jsonPath("$.phoneNumber").value("34988760098"));
    }

    @Test
    void createShouldReturnA409StatusWhenThePhoneNumberIsAlreadyRegistered() throws Exception {
        //Arrange:
        this.customerRepository.save(new Customer(null, "José", "34988760098", "327.197.730-23", null));

        CustomerRequestDto requestDto = new CustomerRequestDto("Maria", "34988760098", "976.566.435-67");
        //Act
        //Assert:
        this.mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void createShouldReturnA409StatusWhenTheCpfIsAlreadyRegistered() throws Exception {
        //Arrange:
        this.customerRepository.save(new Customer(null, "José", "34988760098", "327.197.730-23", null));

        CustomerRequestDto requestDto = new CustomerRequestDto("Maria", "37998657122", "327.197.730-23");
        //Act
        //Assert:
        this.mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict());
    }

//update:
    @Test
    void updateShouldReturnStatus200Successfully() throws Exception {
        //Arrange
        Customer registeredCustomer = this.customerRepository.save(new Customer(null, "Maria", "85884556327", "327.197.730-23", null));

        CustomerRequestDto requestDto = new CustomerRequestDto("Maria", "34988760098", "327.197.730-23");

        //Act
        //Assert
        this.mockMvc.perform(put("/customers/{customerId}", registeredCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maria"))
                .andExpect(jsonPath("$.phoneNumber").value("34988760098"));
    }

    @Test
    void updateShouldReturnStatus404WhenTheCustomerIsNotFound() throws Exception {
        //Arrange:
        Customer registeredCustomer = this.customerRepository.save(new Customer(null, "José", "34988760098", "327.197.730-23", null));
        CustomerRequestDto requestDto = new CustomerRequestDto("José", "85998436627", "327.197.730-23");

        //Act
        //Assert
        this.mockMvc.perform(put("/customers/{customerId}", registeredCustomer.getId() + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateShouldReturnA409StatusWhenThePhoneNumberIsAlreadyRegistered() throws Exception {
        //Arrange:
        Customer registeredCustomer01 = this.customerRepository.save(new Customer(null, "José", "34988760098", "327.197.730-23", null));
        this.customerRepository.save(new Customer(null, "Carlos", "85998436627", "175.553.897-96", null));

        CustomerRequestDto requestDto = new CustomerRequestDto("José", "85998436627", "327.197.730-23");
        //Act
        //Assert:
        this.mockMvc.perform(put("/customers/{customerId}", registeredCustomer01.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateShouldReturnA409StatusWhenTheCpfIsAlreadyRegistered() throws Exception {
        //Arrange:
        Customer registeredCustomer01 = this.customerRepository.save(new Customer(null, "José", "34988760098", "327.197.730-23", null));
        this.customerRepository.save(new Customer(null, "Carlos", "85998436627", "175.553.897-96", null));

        CustomerRequestDto requestDto = new CustomerRequestDto("José", "34988760098", "175.553.897-96");
        //Act
        //Assert:
        this.mockMvc.perform(put("/customers/{customerId}", registeredCustomer01.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict());
    }
    
//deleteById:
    @Test
    void deleteByIdShouldReturnA204Successfuly() throws Exception {
        //Arrange
        Customer registeredCustomer = this.customerRepository.save(new Customer(null, "José", "34988760098", "327.197.730-23", null));

        //Act
        //Assert
        this.mockMvc.perform(delete("/customers/{customerId}", registeredCustomer.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertTrue(this.customerRepository.findById(registeredCustomer.getId()).isEmpty());
    }

    @Test
    void deleteByIdShouldReturnStatus404WhenTheCustomerIsNotFound() throws Exception {
        //Act
        //Assert
        this.mockMvc.perform(delete("/customers/{customerId}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("deleteById() should return 409 status when the entity has a relationship with another entity")
    void deleteByIdShouldReturnA409StatusWhenTheEntityHasARelationship() throws Exception {
        //Arrange
        Customer registeredCustomer = this.customerRepository.save(new Customer(null, "José", "34988760098", "327.197.730-23", null));
        Sale registered = this.saleRepository.save(new Sale(null, SaleStatus.PAID, 100.00F, 1, 100.00F, LocalDateTime.now(), registeredCustomer, null));

        //Act
        //Assert:
        this.mockMvc.perform(delete("/customers/{customerId}", registeredCustomer.getId()))
                .andExpect(status().isConflict());
    }
}