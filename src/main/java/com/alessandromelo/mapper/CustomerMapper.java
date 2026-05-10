package com.alessandromelo.mapper;

import com.alessandromelo.dto.customer.CustomerResponseDto;
import com.alessandromelo.dto.customer.CustomerRequestDto;
import com.alessandromelo.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    //Request -> Entity
    Customer toEntity(CustomerRequestDto customerRequestDto);

    //Entity -> Response
    CustomerResponseDto toResponseDto(Customer customer);
}
