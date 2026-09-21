package com.alessandromelo.service;

import com.alessandromelo.dto.sale.SaleRequestDto;
import com.alessandromelo.dto.sale.SaleResponseDto;
import com.alessandromelo.entity.Customer;
import com.alessandromelo.entity.Sale;
import com.alessandromelo.exception.customer.CustomerNotFoundException;
import com.alessandromelo.exception.sale.SaleNotFoundException;
import com.alessandromelo.mapper.SaleMapper;
import com.alessandromelo.repository.CustomerRepository;
import com.alessandromelo.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;

    private final CustomerRepository customerRepository;

    public SaleService(SaleRepository saleRepository, SaleMapper saleMapper, CustomerRepository customerRepository) {
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
        this.customerRepository = customerRepository;
    }


//GET
    public List<SaleResponseDto> getAll(){

        List<Sale> sales = this.saleRepository.findAll();
        return sales.stream().map(this.saleMapper::toResponse).toList();
    }

//GET
    public SaleResponseDto getById(Long saleId) {

        Sale sale = this.saleRepository.findById(saleId).orElseThrow(
                () -> new SaleNotFoundException(saleId)
        );

        return this.saleMapper.toResponse(sale);
    }

//POST
    @Transactional
    public SaleResponseDto create(SaleRequestDto requestDto){

        Customer customer;

        if(requestDto.getCustomerId() != null){

            customer = this.customerRepository.findById(requestDto.getCustomerId()).orElseThrow(
                    () -> new CustomerNotFoundException(requestDto.getCustomerId())
            );
        }else {
            customer = null;
        }

        Sale sale = this.saleMapper.toEntity(requestDto);
        sale.setCustomer(customer);

        return this.saleMapper.toResponse(this.saleRepository.save(sale));
    }

//PUT
    @Transactional
    public SaleResponseDto update(Long saleId, SaleRequestDto requestDto){

        Sale sale = this.saleRepository.findById(saleId).orElseThrow(
                () -> new SaleNotFoundException(saleId)
        );

        Customer customer = null;

        if(requestDto.getCustomerId() != null){

            customer = this.customerRepository.findById(requestDto.getCustomerId()).orElseThrow(
                    () -> new CustomerNotFoundException(requestDto.getCustomerId())
            );
        }

        sale.setStatus(requestDto.getStatus());
        sale.setTotalAmount(requestDto.getTotalAmount());
        sale.setInstallments(requestDto.getInstallments());
        sale.setInstallmentAmount(requestDto.getInstallmentAmount());
        sale.setSaleDate(requestDto.getSaleDate());
        sale.setCustomer(customer);

        return this.saleMapper.toResponse(this.saleRepository.save(sale));
    }

//DELETE
    @Transactional
    public void deleteById(Long saleId){

        Sale sale = this.saleRepository.findById(saleId).orElseThrow(
                () -> new SaleNotFoundException(saleId)
        );

        this.saleRepository.delete(sale);
    }
}
