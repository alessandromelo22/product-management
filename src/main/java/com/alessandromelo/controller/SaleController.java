package com.alessandromelo.controller;

import com.alessandromelo.dto.sale.SaleDateRequestDto;
import com.alessandromelo.dto.sale.SaleRequestDto;
import com.alessandromelo.dto.sale.SaleResponseDto;
import com.alessandromelo.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Sale", description = "Operations focused on sales management.")
@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }


//GET
    @Operation(
            summary = "Search all sales",
            description = "Returns all registered sales.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sales successfully returned.")
    })
    @GetMapping
    public ResponseEntity<List<SaleResponseDto>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.saleService.getAll());
    }

//GET
    @Operation(
            summary = "Search for a sale by ID",
            description = "Returns a registered sale by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sale successfully returned."),
            @ApiResponse(responseCode = "400", description = "Invalid ID provided."),
            @ApiResponse(responseCode = "404", description = "Sale not found.")
    })
    @GetMapping("/{saleId}")
    public ResponseEntity<SaleResponseDto> getById(@PathVariable Long saleId){
        return ResponseEntity.status(HttpStatus.OK).body(this.saleService.getById(saleId));
    }

//GET
    @Operation(
            summary = "Search for all sales by sale date",
            description = "Returns a registered sales by sale date.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sales successfully returned."),
            @ApiResponse(responseCode = "400", description = "Invalid input data.")
    })
    @GetMapping("/sale-date")
    public ResponseEntity<List<SaleResponseDto>> getBySaleDate(@Valid @RequestBody SaleDateRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.OK).body(this.saleService.getBySaleDate(requestDto));
    }

//POST
    @Operation(
            summary = "Register a new sale",
            description = "Register a new sale in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sale successfully registered."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "404", description = "Customer not found.")
    })
    @PostMapping
    public ResponseEntity<SaleResponseDto> create(@Valid @RequestBody SaleRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.saleService.create(requestDto));
   }

//PUT
    @Operation(
            summary = "Update a sale",
            description = "Updates a sale already registered in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sale successfully updated."),
            @ApiResponse(responseCode = "400", description = "Invalid ID provided."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "404", description = "Sale not found."),
            @ApiResponse(responseCode = "404", description = "Customer not found.")
    })
    @PutMapping("/{saleId}")
    public ResponseEntity<SaleResponseDto> update(@PathVariable Long saleId, @Valid @RequestBody SaleRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.OK).body(this.saleService.update(saleId, requestDto));
    }

//DELETE
    @Operation(
            summary = "Delete a sale by ID",
            description = "Delete a sale already registered in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sale successfully updated."),
            @ApiResponse(responseCode = "400", description = "Invalid ID provided."),
            @ApiResponse(responseCode = "404", description = "Sale not found."),
            @ApiResponse(responseCode = "409", description = "Deletion not performed - Sale is linked to another entity.")
    })
    @DeleteMapping("/{saleId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long saleId){
        this.saleService.deleteById(saleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
