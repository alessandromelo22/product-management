package com.alessandromelo.mapper;

import com.alessandromelo.dto.saleproduct.SaleProductRequestDto;
import com.alessandromelo.dto.saleproduct.SaleProductResponseDto;
import com.alessandromelo.entity.SaleProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleProductMapper {


    //RequestDto -> Entity
    @Mapping(target = "sale", ignore = true)
    @Mapping(target = "product", ignore = true)
    SaleProduct toEntity(SaleProductRequestDto requestDto);

    //Entity -> ResponseDto
    @Mapping(source = "sale.id", target = "saleId")
    @Mapping(source = "product.id", target = "productId")
    SaleProductResponseDto toResponse(SaleProduct saleProduct);
}
