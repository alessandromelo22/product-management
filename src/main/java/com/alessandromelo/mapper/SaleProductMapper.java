package com.alessandromelo.mapper;

import com.alessandromelo.dto.sale.SaleRequestDto;
import com.alessandromelo.dto.saleproduct.SaleProductRequestDto;
import com.alessandromelo.dto.saleproduct.SaleProductResponseDto;
import com.alessandromelo.dto.saleproduct.SaleProductResumeRequestDto;
import com.alessandromelo.dto.saleproduct.SaleProductResumeResponseDto;
import com.alessandromelo.entity.SaleProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleProductMapper {


    //RequestDto -> Entity
    @Mapping(target = "sale", ignore = true)
    @Mapping(target = "product", ignore = true)
    SaleProduct toEntity(SaleProductRequestDto requestDto);

    //SaleRequestDto -> Entity
    @Mapping(target = "sale", ignore = true)
    @Mapping(target = "product", ignore = true)
    SaleProduct toEntity(SaleRequestDto saleRequestDto);

    @Mapping(target = "sale", ignore = true)
    @Mapping(target = "product", ignore = true)
    SaleProduct toEntity(SaleProductResumeRequestDto saleProductResumeRequestDto);

    @Mapping(target = "productId", source = "product.id")
    SaleProductResumeResponseDto toResumeResponse(SaleProduct saleProduct);


    //Entity -> ResponseDto
    @Mapping(target = "saleId", source = "sale.id")
    @Mapping(target = "productId", source = "product.id")
    SaleProductResponseDto toResponse(SaleProduct saleProduct);
}
