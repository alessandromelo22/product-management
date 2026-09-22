package com.alessandromelo.mapper;

import com.alessandromelo.dto.sale.SaleRequestDto;
import com.alessandromelo.dto.sale.SaleResponseDto;
import com.alessandromelo.entity.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleMapper {


    //RequestDto -> Entity
    @Mapping(target = "customer", ignore = true)
    Sale toEntity(SaleRequestDto requestDto);


    //Entity -> ResponseDto
    @Mapping(source = "customer.id", target = "customerId")
    SaleResponseDto toResponse(Sale sale);
}
