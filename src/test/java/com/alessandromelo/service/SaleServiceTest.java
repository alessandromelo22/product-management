package com.alessandromelo.service;

import com.alessandromelo.builders.customer.CustomerBuilder;
import com.alessandromelo.builders.product.ProductBuilder;
import com.alessandromelo.builders.sale.SaleBuilder;
import com.alessandromelo.builders.sale.SaleDateRequestDtoBuilder;
import com.alessandromelo.builders.sale.SaleRequestDtoBuilder;
import com.alessandromelo.builders.sale.SaleResponseDtoBuilder;
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
import com.alessandromelo.exception.product.ProductNotFoundException;
import com.alessandromelo.exception.product.ProductQuantityExceedsStock;
import com.alessandromelo.exception.sale.InstallmentNotValidException;
import com.alessandromelo.exception.sale.SaleAlreadyCanceledException;
import com.alessandromelo.exception.sale.SaleNotFoundException;
import com.alessandromelo.exception.saleproduct.QuantityNotValidException;
import com.alessandromelo.mapper.SaleMapper;
import com.alessandromelo.mapper.SaleProductMapper;
import com.alessandromelo.repository.CustomerRepository;
import com.alessandromelo.repository.ProductRepository;
import com.alessandromelo.repository.SaleProductRepository;
import com.alessandromelo.repository.SaleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @InjectMocks
    private SaleService saleService;
    @Mock
    private SaleRepository saleRepository;
    @Mock
    private SaleMapper saleMapper;
    @Mock
    private SaleProductMapper saleProductMapper;
    @Mock
    private SaleProductRepository saleProductRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ProductRepository productRepository;

    @Captor
    private ArgumentCaptor<Sale> saleCaptor;
    @Captor
    private ArgumentCaptor<Product> productCaptor;
    @Captor
    private ArgumentCaptor<SaleProduct> saleProductCaptor;


//getAll():
    @Test
    public void getAll_noSalesInTheDatabase_returnAnEmptyList (){
        //Arrange:
        Mockito.when(this.saleRepository.findAll()).thenReturn(List.of());

        //Act:
        List<SaleResponseDto> returned = this.saleService.getAll();

        //Assert:
        Assertions.assertNotNull(returned);
        Assertions.assertTrue(returned.isEmpty());
    }

    @Test
    public void getAll_happyPath_returnAListOfSaleResponseDto (){
        //Arrange:
        Sale searchedSale1 = new SaleBuilder().build();
        Sale searchedSale2 = new SaleBuilder()
                .withId(2L)
                .withStatus(SaleStatus.PENDING)
                .withTotalAmount(new BigDecimal("150.00"))
                .withInstallments(1)
                .withInstallmentAmount(new BigDecimal("150.00"))
                .withSaleDate(LocalDateTime.now())
                .withCustomer(null)
                .withSaleProducts(null).build();

        SaleResponseDto responseDto1 = new SaleResponseDtoBuilder().build();
        SaleResponseDto responseDto2 = new SaleResponseDtoBuilder()
                .withId(2L)
                .withSaleProductResumeResponseDtos(null)
                .withStatus(SaleStatus.PENDING)
                .withTotalAmount(new BigDecimal("150.00"))
                .withInstallments(1)
                .withInstallmentAmount(new BigDecimal("150.00"))
                .withSaleDate(LocalDateTime.now())
                .withCustomerId(null).build();

        Mockito.when(this.saleRepository.findAll()).thenReturn(List.of(searchedSale1, searchedSale2));
        Mockito.when(this.saleMapper.toResponse(searchedSale1)).thenReturn(responseDto1);
        Mockito.when(this.saleMapper.toResponse(searchedSale2)).thenReturn(responseDto2);

        //Act:
        List<SaleResponseDto> returned = this.saleService.getAll();

        //Assert:
        Assertions.assertEquals(2, returned.size());
        Assertions.assertSame(responseDto1, returned.get(0));
        Assertions.assertSame(responseDto2, returned.get(1));
    }



//getById:
    @Test
    void getById_saleIdDoesNotExist_throwAndDoesNotReturn(){
        //Arrange:
        Mockito.when(this.saleRepository.findById(999L)).thenReturn(Optional.empty());

        //Act:
        //Assert:
        Assertions.assertThrows(SaleNotFoundException.class,
                () -> this.saleService.getById(999L));

        Mockito.verify(this.saleMapper, Mockito.never()).toResponse(Mockito.any());
    }

    @Test
    void getById_happyPath_returnASaleResponseDtoSuccessfully(){
        //Arrange:
        Sale searchedSale = new SaleBuilder().build();
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.saleRepository.findById(1L)).thenReturn(Optional.of(searchedSale));
        Mockito.when(this.saleMapper.toResponse(searchedSale)).thenReturn(responseDto);

        //Act:
        SaleResponseDto returned = this.saleService.getById(1L);

        //Assert:
        Assertions.assertSame(responseDto, returned);
    }



//getBySaleDate:
    @Test
    public void getBySaleDate_noSalesInTheDatabase_returnAnEmptyList (){
        //Arrange:
        SaleDateRequestDto requestDto = new SaleDateRequestDtoBuilder().build();

        Mockito.when(this.saleRepository.findBySaleDateBetween(requestDto.getStart().atStartOfDay(), requestDto.getEnd().atTime(LocalTime.MAX))).thenReturn(List.of());

        //Act:
        List<SaleResponseDto> returned = this.saleService.getBySaleDate(requestDto);

        //Assert:
        Assertions.assertNotNull(returned);
        Assertions.assertTrue(returned.isEmpty());
    }

    @Test
    public void getBySaleDate_happyPath_returnAListOfSaleResponseDto (){
        //Arrange:
        SaleDateRequestDto requestDto = new SaleDateRequestDtoBuilder()
                .withStart(LocalDate.of(2026, Month.NOVEMBER, 2))
                .withEnd(LocalDate.of(2026, Month.NOVEMBER, 3))
                .build();
        Sale searchedSale1 = new SaleBuilder()
                .withSaleDate(LocalDateTime.of(2026, Month.NOVEMBER, 2, 10, 33)).build();

        Sale searchedSale2 = new SaleBuilder()
                .withSaleDate(LocalDateTime.of(2026, Month.NOVEMBER, 3, 17, 21)).build();

        SaleResponseDto responseDto1 = new SaleResponseDtoBuilder().withSaleDate(LocalDateTime.of(2026, Month.NOVEMBER, 2, 10, 33))
                .build();

        SaleResponseDto responseDto2 = new SaleResponseDtoBuilder().withSaleDate(LocalDateTime.of(2026, Month.NOVEMBER, 3, 17, 21))
                .build();

        Mockito.when(this.saleRepository.findBySaleDateBetween(requestDto.getStart().atStartOfDay(), requestDto.getEnd().atTime(LocalTime.MAX)))
                .thenReturn(List.of(searchedSale1, searchedSale2));

        Mockito.when(this.saleMapper.toResponse(searchedSale1)).thenReturn(responseDto1);
        Mockito.when(this.saleMapper.toResponse(searchedSale2)).thenReturn(responseDto2);

        //Act:
        List<SaleResponseDto> returned = this.saleService.getBySaleDate(requestDto);

        //Assert:
        Assertions.assertEquals(2, returned.size());
        Assertions.assertSame(responseDto1, returned.get(0));
        Assertions.assertSame(responseDto2, returned.get(1));
    }



//create:
    @Test
    public void create_customerIdDoesNotExist_throwsAndNotSave (){
        //Arrange:
        SaleRequestDto requestDto = new SaleRequestDtoBuilder().withCustomerId(999L).build();
        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.empty());

        //Act:
        //Assert:
        Assertions.assertThrows(CustomerNotFoundException.class,
                () -> this.saleService.create(requestDto));

        Mockito.verify(this.saleRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void create_customerIdNull_saleHasANullCustomer (){
        //Arrange:
        SaleRequestDto requestDto = new SaleRequestDtoBuilder().withCustomerId(null).build();
        Sale mappedSale = new SaleBuilder().build();
        Product searchedProduct = new ProductBuilder().build();
        SaleProduct mappedSaleProduct = new SaleProduct(null, 1, new BigDecimal("50.00"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Mockito.verify(this.customerRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(this.saleRepository).save(this.saleCaptor.capture());
        Sale captured = this.saleCaptor.getValue();

        Assertions.assertNull(captured.getCustomer());
    }

    @Test
    public void create_happyPath_setCustomerInSaleSuccessfully (){
        //Arrange:
        SaleRequestDto requestDto = new SaleRequestDtoBuilder().build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().build();
        Product searchedProduct = new ProductBuilder().build();
        SaleProduct mappedSaleProduct = new SaleProduct(null, 1, new BigDecimal("50.00"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Mockito.verify(this.saleRepository).save(this.saleCaptor.capture());
        Sale captured = this.saleCaptor.getValue();

        Assertions.assertSame(searchedCustomer, captured.getCustomer());
    }

    @Test
    public void create_requestDoesNotHaveSaleDate_setSaleDateToTheCurrentDate (){
        //Arrange:
        SaleRequestDto requestDto = new SaleRequestDtoBuilder().withSaleDate(null).build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().withSaleDate(null).build();
        Product searchedProduct = new ProductBuilder().build();
        SaleProduct mappedSaleProduct = new SaleProduct(null, 1, new BigDecimal("50.00"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Assertions.assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), mappedSale.getSaleDate().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    public void create_requestHaveSaleDate_retainsTheSaleDateFromTheRequest (){
        //Arrange:
        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleDate(LocalDateTime.of(2026, Month.NOVEMBER, 4, 10, 23))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder()
                .withSaleDate(LocalDateTime.of(2026, Month.NOVEMBER, 4, 10, 23))
                .build();
        Product searchedProduct = new ProductBuilder().build();
        SaleProduct mappedSaleProduct = new SaleProduct(null, 1, new BigDecimal("50.00"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Assertions.assertEquals(LocalDateTime.of(2026, Month.NOVEMBER, 4, 10, 23), mappedSale.getSaleDate());
    }

    @Test
    public void create_happyPath_setTotalAmountSuccessfullyInTheSale (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto1 = new SaleProductResumeRequestDto(1L, 3, new BigDecimal("25.00"));
        SaleProductResumeRequestDto saleProductResumeRequestDto2 = new SaleProductResumeRequestDto(2L, 2, new BigDecimal("40.00"));

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto1, saleProductResumeRequestDto2))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().withTotalAmount(null).build();
        Product searchedProduct1 = new ProductBuilder().build();
        Product searchedProduct2 = new ProductBuilder().withId(2L).build();
        SaleProduct mappedSaleProduct1 = new SaleProduct(null, 3, new BigDecimal("25.00"), null, null);
        SaleProduct mappedSaleProduct2 = new SaleProduct(null, 2, new BigDecimal("40.00"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct1));
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(1).getProductId())).thenReturn(Optional.of(searchedProduct2));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct1);
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(1))).thenReturn(mappedSaleProduct2);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct1, mappedSaleProduct2))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Assertions.assertEquals(new BigDecimal("155.00"), mappedSale.getTotalAmount());
    }

    @Test
    public void create_quantityZero_throwAndDoesNotSave (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, 0, new BigDecimal("50.00")); //'quantity' zero

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().withTotalAmount(null).build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);

        //Act:
        //Assert:
        Assertions.assertThrows(QuantityNotValidException.class,
                () -> this.saleService.create(requestDto));
    }

    @Test
    public void create_negativeQuantity_throwAndDoesNotSave (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, -5, new BigDecimal("50.00")); //negative 'quantity'

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().withTotalAmount(null).build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);

        //Act:
        //Assert:
        Assertions.assertThrows(QuantityNotValidException.class,
                () -> this.saleService.create(requestDto));
    }

    @Test
    public void create_unitPriceWithMinimumValidValue_setTotalAmountSuccessfullyInTheSale (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, 3, new BigDecimal("0.01")); //'unitPrice' mínimo valido

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().withTotalAmount(null).build();
        Product searchedProduct = new ProductBuilder().build();
        SaleProduct mappedSaleProduct = new SaleProduct(null, 3, new BigDecimal("0.01"), null, null); //'unitPrice' mínimo valido
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Assertions.assertEquals(new BigDecimal("0.03"), mappedSale.getTotalAmount());
    }

    @Test
    public void create_unitPriceWithDifferentScala_setTotalAmountSuccessfullyInTheSale (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, 3, new BigDecimal("50")); //'unitPrice' com escala de 0

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().withTotalAmount(null).build();
        Product searchedProduct = new ProductBuilder().build();
        SaleProduct mappedSaleProduct = new SaleProduct(null, 3, new BigDecimal("50"), null, null); //'unitPrice' com escala de 0
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Assertions.assertEquals(new BigDecimal("150.00"), mappedSale.getTotalAmount()); //esperado que seja retornado um valor com escala de 2
    }

    @Test
    public void create_unitPriceNull_setUnitPriceWithProductPriceAndSetTotalAmountSuccessfullyInTheSale (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, 3, null); //'unitPrice' null

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().withTotalAmount(null).build();
        Product searchedProduct = new ProductBuilder().withPrice(new BigDecimal("50.00")).build(); // Product 'price'
        SaleProduct mappedSaleProduct = new SaleProduct(null, 3, new BigDecimal("50.00"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Assertions.assertEquals(new BigDecimal("50.00"), requestDto.getSaleProductResumeRequestDtos().get(0).getUnitPrice());
        Assertions.assertEquals(new BigDecimal("150.00"), mappedSale.getTotalAmount());
    }

    @Test
    public void create_unitPriceZero_setUnitPriceWithProductPriceAndSetTotalAmountSuccessfullyInTheSale (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, 3, BigDecimal.ZERO); //'unitPrice' zero

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().withTotalAmount(null).build();
        Product searchedProduct = new ProductBuilder().withPrice(new BigDecimal("50.00")).build(); // Product 'price'
        SaleProduct mappedSaleProduct = new SaleProduct(null, 3, new BigDecimal("50.00"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Assertions.assertEquals(new BigDecimal("50.00"), requestDto.getSaleProductResumeRequestDtos().get(0).getUnitPrice());
        Assertions.assertEquals(new BigDecimal("150.00"), mappedSale.getTotalAmount());
    }

    @Test
    public void create_unitPriceNegative_setUnitPriceWithProductPriceAndSetTotalAmountSuccessfullyInTheSale (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, 3, new BigDecimal("-200.00")); //'unitPrice' negativo

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().withTotalAmount(null).build();
        Product searchedProduct = new ProductBuilder().withPrice(new BigDecimal("50.00")).build(); // Product 'price'
        SaleProduct mappedSaleProduct = new SaleProduct(null, 3, new BigDecimal("50.00"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Assertions.assertEquals(new BigDecimal("50.00"), requestDto.getSaleProductResumeRequestDtos().get(0).getUnitPrice());
        Assertions.assertEquals(new BigDecimal("150.00"), mappedSale.getTotalAmount());
    }

    @Test
    public void create_installmentZero_throwAndNotSave (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, 1, new BigDecimal("200.00"));

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .withInstallments(0) //'installments' zero
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().withTotalAmount(null).withInstallments(0) //'installments' zero
                .withInstallmentAmount(BigDecimal.ZERO).build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);

        //Act:
        //Assert:
        Assertions.assertThrows(InstallmentNotValidException.class,
                () -> this.saleService.create(requestDto));

        Mockito.verify(this.saleRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(this.saleProductRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void create_installmentNegative_throwAndNotSave (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, 1, new BigDecimal("200.00"));

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .withInstallments(-1) //'installments' negativo
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().withTotalAmount(null).withInstallments(-1) //'installments' negativo
                .withInstallmentAmount(BigDecimal.ZERO).build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);

        //Act:
        //Assert:
        Assertions.assertThrows(InstallmentNotValidException.class,
                () -> this.saleService.create(requestDto));

        Mockito.verify(this.saleRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(this.saleProductRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void create_happyPath_setInstallmentAmountSuccessfullyInTheSale (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, 1, new BigDecimal("0.25"));

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .withInstallments(2)
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().withTotalAmount(null).withInstallments(2)
                .withInstallmentAmount(BigDecimal.ZERO).build();
        Product searchedProduct = new ProductBuilder().build();
        SaleProduct mappedSaleProduct = new SaleProduct(null, 1, new BigDecimal("0.25"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Assertions.assertEquals(new BigDecimal("0.12"), mappedSale.getInstallmentAmount());
    }

    @Test
    public void create_productIdDoesNotExist_throwsAndDoesNotSave (){
        //Arrange:
        SaleRequestDto requestDto = new SaleRequestDtoBuilder().build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.empty());

        //Act:
        //Assert:
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> this.saleService.create(requestDto));

        Mockito.verify(this.saleRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void create_quantityOfProductExceedStock_throwsAndDoesNotSave (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, 6, new BigDecimal("7.00")); //'quantity' maior que 'stock'

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().build();
        Product searchedProduct = new ProductBuilder().withStock(5).build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));

        //Act:
        //Assert:
        Assertions.assertThrows(ProductQuantityExceedsStock.class,
                () -> this.saleService.create(requestDto));

        Mockito.verify(this.saleRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void create_quantityOfProductEqualsStock_saveSaleSuccessfully (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, 5, new BigDecimal("7.00"));

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().build();
        Product searchedProduct = new ProductBuilder().withStock(5).build();// 'stock' igual a 'quantity'
        SaleProduct mappedSaleProduct = new SaleProduct(null, 5, new BigDecimal("7.00"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Mockito.verify(this.saleRepository).save(mappedSale);
    }

    @Test
    public void create_stockGreaterThanQuantityOfProducts_saveSaleSuccessfully (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, 5, new BigDecimal("7.00"));

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().build();
        Product searchedProduct = new ProductBuilder().withStock(10).build();//'stock' maior que a 'quantity' de produtos
        SaleProduct mappedSaleProduct = new SaleProduct(null, 5, new BigDecimal("7.00"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Mockito.verify(this.saleRepository).save(mappedSale);
    }

    @Test
    public void create_stockGreaterThanQuantityOfProducts_itemsSuccessfullyRemovedFromInventory (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto1 = new SaleProductResumeRequestDto(1L, 5, new BigDecimal("7.00"));
        SaleProductResumeRequestDto saleProductResumeRequestDto2 = new SaleProductResumeRequestDto(2L, 2, new BigDecimal("10.00"));

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto1, saleProductResumeRequestDto2))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().build();
        Product searchedProduct1 = new ProductBuilder().withStock(10).build(); //'stock' maior que a 'quantity' de produtos
        Product searchedProduct2 = new ProductBuilder().withStock(8).build(); //'stock' maior que a 'quantity' de produtos
        SaleProduct mappedSaleProduct1 = new SaleProduct(null, 5, new BigDecimal("7.00"), null, null);
        SaleProduct mappedSaleProduct2 = new SaleProduct(null, 2, new BigDecimal("10.00"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct1));
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(1).getProductId())).thenReturn(Optional.of(searchedProduct2));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct1);
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(1))).thenReturn(mappedSaleProduct2);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct1, mappedSaleProduct2))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Assertions.assertEquals(5, searchedProduct1.getStock());
        Assertions.assertEquals(6, searchedProduct2.getStock());
        Mockito.verify(productRepository).save(searchedProduct1);
        Mockito.verify(productRepository).save(searchedProduct2);
    }

    @Test
    public void create_happyPath_setSaleAndProductInSaleProductSuccessfully (){
        //Arrange:
        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().build();
        Product searchedProduct = new ProductBuilder().build();
        SaleProduct mappedSaleProduct = new SaleProduct(null, 1, new BigDecimal("50.00"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        this.saleService.create(requestDto);

        //Assert:
        Mockito.verify(this.saleProductRepository).save(this.saleProductCaptor.capture());
        SaleProduct captured = this.saleProductCaptor.getValue();

        Assertions.assertSame(mappedSale, captured.getSale());
        Assertions.assertSame(searchedProduct, captured.getProduct());
    }

    @Test
    public void create_happyPath_returnSaleResponseDtoSuccessfully (){
        //Arrange:
        SaleProductResumeRequestDto saleProductResumeRequestDto = new SaleProductResumeRequestDto(1L, 5, new BigDecimal("7.00"));

        SaleRequestDto requestDto = new SaleRequestDtoBuilder()
                .withSaleProductResumeRequestDtos(List.of(saleProductResumeRequestDto))
                .build();
        Customer searchedCustomer = new CustomerBuilder().build();
        Sale mappedSale = new SaleBuilder().build();
        Product searchedProduct = new ProductBuilder().build();
        SaleProduct mappedSaleProduct = new SaleProduct(null, 5, new BigDecimal("10.00"), null, null);
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().build();

        Mockito.when(this.customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(searchedCustomer));
        Mockito.when(this.saleMapper.toEntity(requestDto)).thenReturn(mappedSale);
        Mockito.when(this.productRepository.findById(requestDto.getSaleProductResumeRequestDtos().get(0).getProductId())).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductMapper.toEntity(requestDto.getSaleProductResumeRequestDtos().get(0))).thenReturn(mappedSaleProduct);
        Mockito.when(this.saleMapper.toResponse(mappedSale, List.of(mappedSaleProduct))).thenReturn(responseDto);

        //Act:
        SaleResponseDto returned = this.saleService.create(requestDto);

        //Assert:
        Assertions.assertSame(responseDto, returned);
    }




//updateSaleDate:
    @Test
    public void updateSaleDate_saleIdDoesNotExist_throwAndDoesNotSave() {
        //Arrange:
        SalePatchDateRequestDto requestDto = new SalePatchDateRequestDto(LocalDate.of(2026, Month.NOVEMBER, 3));

        Mockito.when(this.saleRepository.findById(1L)).thenReturn(Optional.empty());

        //Act:
        //Assert:
        Assertions.assertThrows(SaleNotFoundException.class,
                () -> this.saleService.updateSaleDate(1L, requestDto));

        Mockito.verify(this.saleRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(this.saleMapper, Mockito.never()).updateSaleDate(Mockito.any(), Mockito.any());
    }

    @Test
    public void updateSaleDate_happyPath_returnASaleResponse() {
        //Arrange:
        SalePatchDateRequestDto requestDto = new SalePatchDateRequestDto(LocalDate.of(2026, Month.NOVEMBER, 3));
        Sale searchedSale = new SaleBuilder().build();
        Sale saved = new SaleBuilder().withSaleDate(LocalDate.of(2026, Month.NOVEMBER, 3).atStartOfDay()).build();
        SaleResponseDto responseDto = new SaleResponseDtoBuilder().withSaleDate(LocalDate.of(2026, Month.NOVEMBER, 3).atStartOfDay())
                .build();

        Mockito.when(this.saleRepository.findById(1L)).thenReturn(Optional.of(searchedSale));
        Mockito.when(this.saleRepository.save(searchedSale)).thenReturn(saved);
        Mockito.when(this.saleMapper.toResponse(saved)).thenReturn(responseDto);

        //Act:
        SaleResponseDto returned = this.saleService.updateSaleDate(1L, requestDto);

        //Assert:
        Mockito.verify(this.saleMapper).updateSaleDate(requestDto,searchedSale);
        Mockito.verify(this.saleRepository).save(searchedSale);
        Assertions.assertSame(responseDto, returned);
    }




//cancelSale:
    @Test
    public void cancelSale_saleIdDoesNotExist_throwAndDoesNotSave (){
        //Arrange:
        Mockito.when(this.saleRepository.findById(1L)).thenReturn(Optional.empty());

        //Act:
        //Assert:
        Assertions.assertThrows(SaleNotFoundException.class,
                () -> this.saleService.cancelSale(1L));
        Mockito.verify(this.productRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(this.saleRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void cancelSale_saleAlreadyCanceled_throwAndDoesNotSave (){
        //Arrange:
        Sale searchedSale = new SaleBuilder().withStatus(SaleStatus.CANCELLED).build();

        Mockito.when(this.saleRepository.findById(1L)).thenReturn(Optional.of(searchedSale));

        //Act:
        //Assert:
        Assertions.assertThrows(SaleAlreadyCanceledException.class,
                () -> this.saleService.cancelSale(1L));
        Mockito.verify(this.productRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(this.saleRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void cancelSale_happyPath_updatesProductStockSuccessfully (){
        //Arrange:
        Product product1 = new ProductBuilder().withStock(25).build();
        Product product2 = new ProductBuilder().withStock(10).build();
        SaleProduct saleProduct1 = new SaleProduct(1L, 10, null, null, product1);
        SaleProduct saleProduct2 = new SaleProduct(2L, 5, null, null, product2);
        Sale searchedSale = new SaleBuilder().withSaleProducts(List.of(saleProduct1, saleProduct2)).build();

        Mockito.when(this.saleRepository.findById(1L)).thenReturn(Optional.of(searchedSale));

        //Act:
        this.saleService.cancelSale(1L);

        //Assert:
        Mockito.verify(this.productRepository, Mockito.times(2)).save(this.productCaptor.capture());
        List<Product> captured = this.productCaptor.getAllValues();
        Assertions.assertEquals(35, captured.get(0).getStock());
        Assertions.assertEquals(15, captured.get(1).getStock());
    }

    @Test
    public void cancelSale_happyPath_updatesSaleStatusSuccessfully (){
        //Arrange:
        Product product = new ProductBuilder().withStock(25).build();
        SaleProduct saleProduct = new SaleProduct(1L, 10, null, null, product);
        Sale searchedSale = new SaleBuilder().withSaleProducts(List.of(saleProduct)).build();

        Mockito.when(this.saleRepository.findById(1L)).thenReturn(Optional.of(searchedSale));

        //Act:
        this.saleService.cancelSale(1L);

        //Assert:
        Mockito.verify(this.saleRepository).save(this.saleCaptor.capture());
        Sale captured = this.saleCaptor.getValue();
        Assertions.assertEquals(SaleStatus.CANCELLED, captured.getStatus());
    }
}