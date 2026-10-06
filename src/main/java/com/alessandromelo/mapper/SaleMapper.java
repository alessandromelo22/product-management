package com.alessandromelo.mapper;

import com.alessandromelo.dto.sale.SalePatchDateRequestDto;
import com.alessandromelo.dto.sale.SaleRequestDto;
import com.alessandromelo.dto.sale.SaleResponseDto;
import com.alessandromelo.entity.Sale;
import com.alessandromelo.entity.SaleProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
//"se o campo do DTO vier nulo, não sobrescreve o valor que já está na entidade"
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = SaleProductMapper.class)
public interface SaleMapper {


    //RequestDto -> Entity
    @Mapping(target = "customer", ignore = true)
    Sale toEntity(SaleRequestDto requestDto);

    //Entity -> ResponseDto
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "saleProductResumeResponseDtos", source = "saleProducts")
    SaleResponseDto toResponse(Sale sale);


    //Sale, List<SaleProduct> -> ResponseDto
    @Mapping(target = "customerId", source = "sale.customer.id")
    @Mapping(target = "saleProductResumeResponseDtos", source = "saleProducts")
    SaleResponseDto toResponse(Sale sale, List<SaleProduct> saleProducts);

    //Atualiza a entidade marcada com @MappingTarget com o DTO de entrada
    void updateSaleDate(SalePatchDateRequestDto requestDto, @MappingTarget Sale sale);
}
