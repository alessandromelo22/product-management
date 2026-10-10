package com.alessandromelo.service;

import com.alessandromelo.dto.sale.SaleDateRequestDto;
import com.alessandromelo.dto.sale.SalePatchDateRequestDto;
import com.alessandromelo.dto.sale.SaleRequestDto;
import com.alessandromelo.dto.sale.SaleResponseDto;
import com.alessandromelo.dto.saleproduct.SaleProductResumeRequestDto;
import com.alessandromelo.entity.Customer;
import com.alessandromelo.entity.Product;
import com.alessandromelo.entity.Sale;
import com.alessandromelo.entity.SaleProduct;
import com.alessandromelo.enums.SaleStatus;
import com.alessandromelo.exception.customer.CustomerNotFoundException;
import com.alessandromelo.exception.global.EntityInUseException;
import com.alessandromelo.exception.product.ProductNotFoundException;
import com.alessandromelo.exception.product.ProductQuantityExceedsStock;
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
    private BigDecimal calculatesTheTotalAmount(List<SaleProductResumeRequestDto> saleProductResumeRequestDtos){

        BigDecimal totalAmount = null;

        for (SaleProductResumeRequestDto saleProductResumeRequestDto : saleProductResumeRequestDtos) {
            // se 'unitPrice' nao for informado sera settado com o valor de Product.price
            if(saleProductResumeRequestDto.getUnitPrice() == null){
                saleProductResumeRequestDto.setUnitPrice(this.productRepository.findById(saleProductResumeRequestDto.getProductId()).get().getPrice());
            }

            totalAmount.add(saleProductResumeRequestDto.getUnitPrice().multiply(BigDecimal.valueOf(saleProductResumeRequestDto.getQuantity())));
        }
        return totalAmount;
    }

//Calcula 'installmentAmount' de acordo com a quantidade de 'installments':
    private BigDecimal calculatesTheInstallmentAmount(BigDecimal totalAmount, Integer installments){
        return totalAmount.divide(BigDecimal.valueOf(installments));
    }


//GET
    public List<SaleResponseDto> getAll(){

        List<Sale> sales = this.saleRepository.findAll();
        return sales.stream().map(this.saleMapper::toResponse).toList();
    }

//GET (testar para ver se o problema de N+1 foi resolvido)
    public SaleResponseDto getById(Long saleId) {

        Sale sale = this.saleRepository.findById(saleId).orElseThrow(
                () -> new SaleNotFoundException(saleId)
        );

        return this.saleMapper.toResponse(sale);
    }

//GET (testar para ver se o problema de N+1 foi resolvido)
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
        //setta 'saleDate' se nao for passado nenhuma data na requisição
        if(sale.getSaleDate() == null){
            sale.setSaleDate(LocalDateTime.now());
        }

        //setta o 'totalAmount' de Sale:
        sale.setTotalAmount(this.calculatesTheTotalAmount(requestDto.getSaleProductResumeRequestDtos()));

        //setta o 'installmentAmount' de Sale:
        sale.setInstallmentAmount(this.calculatesTheInstallmentAmount(sale.getTotalAmount(), sale.getInstallments()));

        List<SaleProduct> saleProductList = new ArrayList<>();
        //Product: (loop para percorrer pelo Product de cada SaleProductResumeRequestDto da List e validar se ele existe no banco)
        for(int i = 0; i < requestDto.getSaleProductResumeRequestDtos().size(); i++){

            int finalI = i;
            Product product = this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(i).getProductId()).orElseThrow(
                    () -> new ProductNotFoundException(requestDto.getSaleProductResumeRequestDtos().get(finalI).getProductId())
            );

            //Verifica se a 'quantity' do produto na Sale é compativel com o estoque:
            if (requestDto.getSaleProductResumeRequestDtos().get(i).getQuantity() > product.getStock()){
                throw new ProductQuantityExceedsStock(product.getName(), product.getStock());
            }

            //SaleProduct:
            SaleProduct saleProduct = this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(i));

            //setta a Sale e Product em SaleProduct
            saleProduct.setSale(sale);
            saleProduct.setProduct(product);

            this.saleProductRepository.save(saleProduct);
            saleProductList.add(saleProduct);
        }
        this.saleRepository.save(sale);

        return this.saleMapper.toResponse(sale, saleProductList);
    }

//PATCH:
    @Transactional
    public SaleResponseDto updateSaleDate(Long saleId, SalePatchDateRequestDto requestDto){

        Sale sale = this.saleRepository.findById(saleId).orElseThrow(
                () -> new SaleNotFoundException(saleId)
        );

        this.saleMapper.updateSaleDate(requestDto, sale);

        return this.saleMapper.toResponse(this.saleRepository.save(sale));
    }

//PATCH (testar tanto o endpoint quanto o problema de N+1):
    @Transactional
    public void cancelSale(Long saleId){

        Sale sale = this.saleRepository.findById(saleId).orElseThrow(
                () -> new SaleNotFoundException(saleId)
        );

        for (int i = 0; i< sale.getSaleProducts().size(); i++){

            Product product = sale.getSaleProducts().get(i).getProduct();
            product.setStock(product.getStock() + sale.getSaleProducts().get(i).getQuantity());

            this.productRepository.save(product);
        }
        
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
