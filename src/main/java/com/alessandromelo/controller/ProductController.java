package com.alessandromelo.controller;

import com.alessandromelo.dto.product.ProductRequestDto;
import com.alessandromelo.dto.product.ProductResponseDto;
import com.alessandromelo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//GET
    @Operation(
            summary = "Search all products",
            description = "Returns all registered products.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products successfully returned.")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.productService.getAll());
    }

//GET
    @Operation(
            summary = "Search for a product by ID",
            description = "Returns a registered product by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully returned."),
            @ApiResponse(responseCode = "404", description = "Product not found."),
            @ApiResponse(responseCode = "400", description = "Invalid ID provided.")

    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long productId){
        return ResponseEntity.status(HttpStatus.OK).body(this.productService.getById(productId));
    }


    @Operation(
            summary = "Register a new product",
            description = "Register a new product in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product successfully registered."),
            @ApiResponse(responseCode = "400", description = "Invalid input data.")
    })
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody @Valid ProductRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.create(requestDto));
    }


    @Operation(
            summary = "Update a product",
            description = "Updates a product already registered in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully updated."),
            @ApiResponse(responseCode = "400", description = "Invalid ID provided."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "404", description = "Customer not found.")
    })
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long productId, @RequestBody @Valid ProductRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.OK).body(this.productService.update(productId, requestDto));
    }

    @Operation(
            summary = "Delete a product",
            description = "Delete a product already registered in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully updated."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "404", description = "Customer not found."),
            @ApiResponse(responseCode = "409", description = "Deletion not performed - Product is linked to another entity.")

    })
    @DeleteMapping("/{productId}")//204
    public ResponseEntity<Void> deleteById(@PathVariable Long productId){
        this.productService.deleteById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
