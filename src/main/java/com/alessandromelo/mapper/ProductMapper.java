package com.alessandromelo.mapper;

import com.alessandromelo.dto.product.ProductRequestDto;
import com.alessandromelo.dto.product.ProductResponseDto;
import com.alessandromelo.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    //Request -> Entity
    Product toEntity(ProductRequestDto requestDto);

    //Entity -> ResponseDto
    ProductResponseDto toResponseDto(Product product);
}
