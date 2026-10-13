package com.alessandromelo.service;

import com.alessandromelo.dto.saleproduct.SaleProductRequestDto;
import com.alessandromelo.dto.saleproduct.SaleProductResponseDto;
import com.alessandromelo.entity.Product;
import com.alessandromelo.entity.Sale;
import com.alessandromelo.entity.SaleProduct;
import com.alessandromelo.exception.product.ProductNotFoundException;
import com.alessandromelo.exception.sale.SaleNotFoundException;
import com.alessandromelo.exception.saleproduct.SaleProductNotFoundException;
import com.alessandromelo.mapper.SaleProductMapper;
import com.alessandromelo.repository.ProductRepository;
import com.alessandromelo.repository.SaleProductRepository;
import com.alessandromelo.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleProductService {

    private final SaleProductRepository saleProductRepository;
    private final SaleProductMapper saleProductMapper;

    private final SaleRepository saleRepository;

    private final ProductRepository productRepository;

    public SaleProductService(SaleProductRepository saleProductRepository, SaleProductMapper saleProductMapper, SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleProductRepository = saleProductRepository;
        this.saleProductMapper = saleProductMapper;
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }


//GET
    public List<SaleProductResponseDto> getAll(){

        List<SaleProduct> saleProducts = this.saleProductRepository.findAll();

        return saleProducts.stream().map(this.saleProductMapper::toResponse).toList();
    }

//GET
    public SaleProductResponseDto getById(Long saleProductId){

        SaleProduct saleProduct = this.saleProductRepository.findById(saleProductId).orElseThrow(
                () -> SaleProductNotFoundException.bySaleProductId(saleProductId)
        );

        return this.saleProductMapper.toResponse(saleProduct);
    }

//POST
    @Transactional
    public SaleProductResponseDto create(SaleProductRequestDto requestDto){

        Sale sale = this.saleRepository.findById(requestDto.getSaleId()).orElseThrow(
                () -> new SaleNotFoundException(requestDto.getSaleId())
        );

        Product product = this.productRepository.findById(requestDto.getProductId()).orElseThrow(
                () -> new ProductNotFoundException(requestDto.getProductId())
        );

        SaleProduct newSaleProduct = this.saleProductMapper.toEntity(requestDto);

        newSaleProduct.setSale(sale);
        newSaleProduct.setProduct(product);

        return this.saleProductMapper.toResponse(this.saleProductRepository.save(newSaleProduct));
    }
}
