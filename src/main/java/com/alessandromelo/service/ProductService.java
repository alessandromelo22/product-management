package com.alessandromelo.service;

import com.alessandromelo.dto.product.ProductEntryToStockRequestDto;
import com.alessandromelo.dto.product.ProductRequestDto;
import com.alessandromelo.dto.product.ProductResponseDto;
import com.alessandromelo.entity.Product;
import com.alessandromelo.exception.global.EntityInUseException;
import com.alessandromelo.exception.product.InvalidStockEntryValueException;
import com.alessandromelo.exception.product.ProductNotFoundException;
import com.alessandromelo.exception.product.ProductPriceInvalidException;
import com.alessandromelo.exception.product.ProductStockInvalidException;
import com.alessandromelo.mapper.ProductMapper;
import com.alessandromelo.repository.ProductRepository;
import com.alessandromelo.repository.SaleProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final SaleProductRepository saleProductRepository;


    public ProductService(ProductRepository productRepository, ProductMapper productMapper, SaleProductRepository saleProductRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.saleProductRepository = saleProductRepository;
    }


//GET
    public List<ProductResponseDto> getAll(){
        return this.productRepository.findAll().stream().map(this.productMapper::toResponseDto).toList();
    }

//GET
    public ProductResponseDto getById(Long productId){

        Product product = this.productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException(productId)
        );

        return this.productMapper.toResponseDto(product);
    }

//POST
    @Transactional
    public ProductResponseDto create(ProductRequestDto requestDto){

        if (requestDto.getPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new ProductPriceInvalidException();
        }
        if (requestDto.getStock() < 0){
            throw new ProductStockInvalidException();
        }
        Product product = this.productMapper.toEntity(requestDto);

        return this.productMapper.toResponseDto(this.productRepository.save(product));
    }

//PUT
    @Transactional
    public ProductResponseDto update(Long productId, ProductRequestDto requestDto){

        return this.productRepository.findById(productId).map(product -> {

            if (requestDto.getPrice().compareTo(BigDecimal.ZERO) <= 0){
                throw new ProductPriceInvalidException();
            }
            if (requestDto.getStock() < 0){
                throw new ProductStockInvalidException();
            }

            product.setName(requestDto.getName());
            product.setBrand(requestDto.getBrand());
            product.setProductCategory(requestDto.getProductCategory());
            product.setPrice(requestDto.getPrice());
            product.setStock(requestDto.getStock());

            return this.productMapper.toResponseDto(this.productRepository.save(product));

        }).orElseThrow(() -> new ProductNotFoundException(productId));
    }

//PATCH:
    @Transactional
    public ProductResponseDto addEntryToStock(Long productId, ProductEntryToStockRequestDto requestDto){

        return this.productRepository.findById(productId).map(
                product -> {

                    if (requestDto.getInputQuantity() <= 0){
                        throw new InvalidStockEntryValueException();
                    }

                    product.setStock(product.getStock() + requestDto.getInputQuantity());

                    return this.productMapper.toResponseDto(this.productRepository.save(product));
                }
        ).orElseThrow(() -> new ProductNotFoundException(productId));
    }

//DELETE
    @Transactional
    public void deleteById(Long productId){

        Product product = this.productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException(productId)
        );

        boolean hasSaleProducts = this.saleProductRepository.existsByProductId(productId);

        if(hasSaleProducts){
            throw new EntityInUseException(Product.class, productId);
        }
        this.productRepository.delete(product);
    }
}
