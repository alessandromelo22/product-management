package com.alessandromelo.service;

import com.alessandromelo.dto.customer.CustomerRequestDto;
import com.alessandromelo.dto.customer.CustomerResponseDto;
import com.alessandromelo.entity.Customer;
import com.alessandromelo.exception.customer.CpfAlreadyExistsException;
import com.alessandromelo.exception.customer.CustomerNotFoundException;
import com.alessandromelo.exception.customer.PhoneNumberAlreadyExistsException;
import com.alessandromelo.exception.global.EntityInUseException;
import com.alessandromelo.mapper.CustomerMapper;
import com.alessandromelo.repository.CustomerRepository;
import com.alessandromelo.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;

    private SaleRepository saleRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, SaleRepository saleRepository){
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.saleRepository = saleRepository;
    }



//GET
    public List<CustomerResponseDto> getAll(){

        List<Customer> customers = this.customerRepository.findAll();
        return customers.stream().map(this.customerMapper::toResponseDto).toList();
    }

//GET
    public CustomerResponseDto getById(Long customerId){

        Customer customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        return this.customerMapper.toResponseDto(customer);
    }

//POST
    public CustomerResponseDto create(CustomerRequestDto requestDto){

        boolean phoneNumberExists = this.customerRepository.existsByPhoneNumber(requestDto.getPhoneNumber());
        boolean cpfExists = this.customerRepository.existsByCpf(requestDto.getCpf());

        if(phoneNumberExists){
            throw new PhoneNumberAlreadyExistsException(requestDto.getPhoneNumber());
        } if (cpfExists){
            throw new CpfAlreadyExistsException(requestDto.getCpf());
        }

        Customer customer = this.customerMapper.toEntity(requestDto);

        return this.customerMapper.toResponseDto(this.customerRepository.save(customer));
    }

//PUT
    public CustomerResponseDto update(Long customerId, CustomerRequestDto requestDto){

        return this.customerRepository.findById(customerId)
                .map(customer -> {

                    boolean phoneNumberExists = this.customerRepository.existsByPhoneNumberAndIdNot(requestDto.getPhoneNumber(), customerId);
                    boolean cpfExists = this.customerRepository.existsByCpfAndIdNot(requestDto.getCpf(), customerId);

                    if(phoneNumberExists){
                        throw new PhoneNumberAlreadyExistsException(requestDto.getPhoneNumber());
                    } else if (cpfExists) {
                        throw new CpfAlreadyExistsException(requestDto.getCpf());
                    }

                    customer.setName(requestDto.getName());
                    customer.setPhoneNumber(requestDto.getPhoneNumber());
                    customer.setCpf(requestDto.getCpf());

                    return this.customerMapper.toResponseDto(this.customerRepository.save(customer));

                }).orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

//DELETE
    public void deleteById(Long customerId){

        Customer customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        boolean hasSales = this.saleRepository.existsByCustomerId(customerId);

        if(hasSales){
            throw new EntityInUseException(Customer.class, customerId);
        }
        this.customerRepository.delete(customer);
    }
}
