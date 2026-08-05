package com.alessandromelo.controller;

import com.alessandromelo.dto.customer.CustomerRequestDto;
import com.alessandromelo.dto.customer.CustomerResponseDto;
import com.alessandromelo.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customer", description = "Operations focused on customer management.")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

//GET:
    @Operation(
        summary = "Search all customers",
        description = "Returns all registered customers.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Customers successfully returned.")
    })
    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAll(){
        return ResponseEntity.ok(this.customerService.getAll());
    }

//GET:
    @Operation(
        summary = "Search for a customer by ID",
        description = "Returns a registered Customer by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer successfully returned."),
            @ApiResponse(responseCode = "404", description = "Customer not found."),
            @ApiResponse(responseCode = "409", description = "Invalid ID provided.")

    })
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> getById(@PathVariable Long customerId){
        return ResponseEntity.ok(this.customerService.getById(customerId));
    }

//POST:
    @Operation(
            summary = "Register a new customer",
            description = "Register a new customer in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer successfully registered."),
            @ApiResponse(responseCode = "409", description = "Invalid input data.")
    })
    @PostMapping
    public ResponseEntity<CustomerResponseDto> create(@RequestBody @Valid CustomerRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.customerService.create(requestDto));
    }

//PUT
    @Operation(
            summary = "Update a customer by ID",
            description = "Update the data of an existing customer.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated successfully."),
            @ApiResponse(responseCode = "404", description = "Customer not found."),
            @ApiResponse(responseCode = "409", description = "Invalid ID provided."),
            @ApiResponse(responseCode = "409", description = "Invalid input data.")

    })
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> update(@PathVariable Long customerId, @RequestBody @Valid CustomerRequestDto requestDto){
        return ResponseEntity.ok(this.customerService.update(customerId, requestDto));
    }

//DELETE
    @Operation(
            summary = "Delete a customer by ID.",
            description = "Deletes a customer from the database by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client successfully deleted."),
            @ApiResponse(responseCode = "404", description = "Customer not found."),
            @ApiResponse(responseCode = "409", description = "Invalid ID provided."),
            @ApiResponse(responseCode = "409", description = "Deletion not performed - Client is linked to another entity.")
    })
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long customerId){
        this.customerService.deleteById(customerId);
        return ResponseEntity.noContent().build(); //204 409
    }

}
