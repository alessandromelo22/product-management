package com.alessandromelo.service;

import com.alessandromelo.dto.sale.SaleDateRequestDto;
import com.alessandromelo.dto.sale.SalePatchDateRequestDto;
import com.alessandromelo.dto.sale.SaleRequestDto;
import com.alessandromelo.dto.sale.SaleResponseDto;
import com.alessandromelo.entity.Customer;
import com.alessandromelo.entity.Product;
import com.alessandromelo.entity.Sale;
import com.alessandromelo.entity.SaleProduct;
import com.alessandromelo.enums.SaleStatus;
import com.alessandromelo.exception.customer.CustomerNotFoundException;
import com.alessandromelo.exception.global.EntityInUseException;
import com.alessandromelo.exception.product.ProductNotFoundException;
import com.alessandromelo.exception.sale.SaleNotFoundException;
import com.alessandromelo.mapper.SaleMapper;
import com.alessandromelo.mapper.SaleProductMapper;
import com.alessandromelo.repository.CustomerRepository;
import com.alessandromelo.repository.ProductRepository;
import com.alessandromelo.repository.SaleProductRepository;
import com.alessandromelo.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;

    private final SaleProductMapper saleProductMapper;
    private final SaleProductRepository saleProductRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;


    public SaleService(SaleRepository saleRepository, SaleMapper saleMapper, SaleProductMapper saleProductMapper, CustomerRepository customerRepository, SaleProductRepository saleProductRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
        this.saleProductMapper = saleProductMapper;
        this.customerRepository = customerRepository;
        this.saleProductRepository = saleProductRepository;
        this.productRepository = productRepository;
    }


//Calcular 'totalAmount'
    private BigDecimal calculatesTheTotalAmount(Integer quantity, BigDecimal unitPrice){

        return unitPrice.multiply(BigDecimal.valueOf(quantity));
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

//GET
    public List<SaleResponseDto> getBySaleDate(SaleDateRequestDto requestDto){

        //converte para o inicio da data (00:00:00)
        LocalDateTime start = requestDto.getStart().atStartOfDay();
        //converte para o fim da data(23:59:59.999999999)
        LocalDateTime end = requestDto.getEnd().atTime(LocalTime.MAX);

        List<Sale> sales = this.saleRepository.findBySaleDateBetween(start, end);

        return sales.stream().map(this.saleMapper::toResponse).toList();
    }

//POST
    @Transactional
    public SaleResponseDto create(SaleRequestDto requestDto){

        //Customer validation
        Customer customer;

        if(requestDto.getCustomerId() != null){

            customer = this.customerRepository.findById(requestDto.getCustomerId()).orElseThrow(
                    () -> new CustomerNotFoundException(requestDto.getCustomerId())
            );
        }else {
            customer = null;
        }

        //Sale
        Sale sale = this.saleMapper.toEntity(requestDto);
        sale.setCustomer(customer);

        if(sale.getSaleDate() == null){
            sale.setSaleDate(LocalDateTime.now());
        }


        List<SaleProduct> saleProductList = new ArrayList<>();
        //Product
        for(int i = 0; i < requestDto.getSaleProductResumeRequestDtos().size(); i++){

            int finalI = i;
            Product product = this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(i).getProductId()).orElseThrow(
                    () -> new ProductNotFoundException(requestDto.getSaleProductResumeRequestDtos().get(finalI).getProductId())
            );

            //SaleProduct
            SaleProduct saleProduct = this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(i));
            saleProduct.setSale(sale);
            saleProduct.setProduct(product);

            this.saleProductRepository.save(saleProduct);
            saleProductList.add(saleProduct);
        }
        this.saleRepository.save(sale);

        return this.saleMapper.toResponse(sale, saleProductList);
    }

//PUT
    @Transactional
    public SaleResponseDto update(Long saleId, SaleRequestDto requestDto){

        return this.saleRepository.findById(saleId).map(
                sale -> {

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
        ).orElseThrow(() -> new SaleNotFoundException(saleId));

    }


//PATCH:
    @Transactional
    public SaleResponseDto updateDate(Long saleId, SalePatchDateRequestDto requestDto){

        Sale sale = this.saleRepository.findById(saleId).orElseThrow(
                () -> new SaleNotFoundException(saleId)
        );

        this.saleMapper.updateSaleDate(requestDto, sale);

        return this.saleMapper.toResponse(this.saleRepository.save(sale));
    }

//PATCH:
    @Transactional
    public void cancelSale(Long saleId){

        Sale sale = this.saleRepository.findById(saleId).orElseThrow(
                () -> new SaleNotFoundException(saleId)
        );

        sale.setStatus(SaleStatus.CANCELLED);
        this.saleRepository.save(sale);
    }

//DELETE
    @Transactional
    public void deleteById(Long saleId){

        Sale sale = this.saleRepository.findById(saleId).orElseThrow(
                () -> new SaleNotFoundException(saleId)
        );

        // Procuro dentro da tabela que a entidade Sale se relaciona (no caso a SaleProduct) se possui algum registro que
        // esta ligado a entidade que eu estou tentando deletar
        boolean hasSaleProducts = this.saleProductRepository.existsBySaleId(saleId);

        if (hasSaleProducts){
            throw new EntityInUseException(Sale.class, saleId);
        }

        this.saleRepository.delete(sale);
    }
}
