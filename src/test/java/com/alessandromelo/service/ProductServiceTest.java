package com.alessandromelo.service;

import com.alessandromelo.builders.product.ProductBuilder;
import com.alessandromelo.builders.product.ProductRequestDtoBuilder;
import com.alessandromelo.builders.product.ProductResponseDtoBuilder;
import com.alessandromelo.dto.product.ProductEntryToStockRequestDto;
import com.alessandromelo.dto.product.ProductRequestDto;
import com.alessandromelo.dto.product.ProductResponseDto;
import com.alessandromelo.entity.Product;
import com.alessandromelo.enums.ProductCategory;
import com.alessandromelo.exception.global.EntityInUseException;
import com.alessandromelo.exception.product.*;
import com.alessandromelo.mapper.ProductMapper;
import com.alessandromelo.repository.ProductRepository;
import com.alessandromelo.repository.SaleProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private SaleProductRepository saleProductRepository;
    @Captor
    private ArgumentCaptor<Product> productCaptor;

//getAll()
    @Test
    void getAll_noProductsInTheDatabase_returnAnEmptyList (){
        //Arrange:
        Mockito.when(this.productRepository.findAll()).thenReturn(List.of());

        //Act:
        List<ProductResponseDto> returned = this.productService.getAll();

        //Assert:
        Assertions.assertNotNull(returned);
        Assertions.assertTrue(returned.isEmpty());
    }

    @Test
    void getAll_happyPath_returnAListOfProductResponseDto (){
        //Arrange:
        Product searchedProduct1 = new ProductBuilder().build();
        Product searchedProduct2 = new ProductBuilder()
                .withId(2L)
                .withName("Perfume")
                .withBrand("Oui")
                .withProductCategory(ProductCategory.FRAGRANCE)
                .withPrice(new BigDecimal("150.00"))
                .withStock(12).build();
        ProductResponseDto responseDto1 = new ProductResponseDtoBuilder().build();
        ProductResponseDto responseDto2 = new ProductResponseDtoBuilder().withId(2L)
                .withName("Perfume")
                .withBrand("Oui")
                .withProductCategory(ProductCategory.FRAGRANCE)
                .withPrice(new BigDecimal("150.00"))
                .withStock(12).build();
        Mockito.when(this.productRepository.findAll()).thenReturn(List.of(searchedProduct1, searchedProduct2));
        Mockito.when(this.productMapper.toResponseDto(searchedProduct1)).thenReturn(responseDto1);
        Mockito.when(this.productMapper.toResponseDto(searchedProduct2)).thenReturn(responseDto2);

        //Act:
        List<ProductResponseDto> returned = this.productService.getAll();

        //Assert:
        Assertions.assertEquals(2, returned.size());
        Assertions.assertSame(responseDto1, returned.get(0));
        Assertions.assertSame(responseDto2, returned.get(1));
    }

//getById():
    @Test
    void getById_productIdDoesNotExist_throwAndDoesNotReturn(){
        //Arrange:
        Mockito.when(this.productRepository.findById(999L)).thenReturn(Optional.empty());

        //Act:
        //Assert:
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> this.productService.getById(999L));

        Mockito.verify(this.productMapper, Mockito.never()).toResponseDto(Mockito.any());
    }

    @Test
    void getById_happyPath_returnAProductResponseDto(){
        //Arrange:
        Product searchedProduct = new ProductBuilder().build();
        ProductResponseDto responseDto = new ProductResponseDtoBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.productMapper.toResponseDto(searchedProduct)).thenReturn(responseDto);

        //Act:
        ProductResponseDto returned = this.productService.getById(1L);

        //Assert:
        Assertions.assertSame(responseDto, returned);
    }

//create():
    @Test
    void create_happyPath_saveProductAndReturnSuccessfully(){
        //Arrange:
        ProductRequestDto requestDto = new ProductRequestDtoBuilder().build();
        Product mapped = new ProductBuilder().withId(null).build();
        Product saved = new ProductBuilder().build();
        ProductResponseDto responseDto = new ProductResponseDtoBuilder().build();

        Mockito.when(this.productMapper.toEntity(requestDto)).thenReturn(mapped);
        Mockito.when(this.productRepository.save(mapped)).thenReturn(saved);
        Mockito.when(this.productMapper.toResponseDto(saved)).thenReturn(responseDto);

        //Act:
        ProductResponseDto returned = this.productService.create(requestDto);

        //Assert:
        Assertions.assertNotNull(returned);
        Assertions.assertSame(responseDto, returned);
        Mockito.verify(productRepository).save(mapped);
    }

    @Test
    void create_negativePrice_throwsAndDoesNotSave (){
        //Arrange:
        ProductRequestDto requestDto = new ProductRequestDtoBuilder().withPrice(new BigDecimal(-1)).build();

        //Act:
        Assertions.assertThrows(ProductPriceInvalidException.class,
                () -> this.productService.create(requestDto));

        //Assert:
        Mockito.verify(this.productRepository, Mockito.never()).save(Mockito.any());
    }

    //Deve lançar a exceção de preço invalido quando receber o valor limite invalido (0)
    // price deve ser > 0 , entao o valor limite invalido é 0
    @Test
    void create_priceZero_throwsAndDoesNotSave(){
        //Arrange:
        ProductRequestDto requestDto = new ProductRequestDtoBuilder().withPrice(new BigDecimal(0)).build(); //valor limite de 'price' invalido (0)

        //Act:
        Assertions.assertThrows(ProductPriceInvalidException.class,
                () -> this.productService.create(requestDto));

        //Assert:
        Mockito.verify(this.productRepository, Mockito.never()).save(Mockito.any());
    }


    //Deve salvar quando o receber o valor limite válido (0.01)
    @Test
    void create_validLimitPrice_saveAProductSuccessfully (){
        //Arrange:
        ProductRequestDto requestDto = new ProductRequestDtoBuilder().withPrice(new BigDecimal("0.01")).build(); //valor limite de price válido (0.01)
        Product mapped = new ProductBuilder().withId(null).build();

        Mockito.when(this.productMapper.toEntity(requestDto)).thenReturn(mapped);

        //Act:
        this.productService.create(requestDto);

        //Assert:
        Mockito.verify(productRepository).save(mapped);
    }

    @Test
    void create_negativeStock_throwsAndDoesNotSave (){
        //Arrange:
        ProductRequestDto requestDto = new ProductRequestDtoBuilder().withStock(-1).build(); //valor limite de 'stock' inválido (-1)

        //Act:
        Assertions.assertThrows(ProductStockInvalidException.class,
                () -> this.productService.create(requestDto));

        //Assert:
        Mockito.verify(this.productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void create_stockZero_saveAProductSuccessfully (){
        //Arrange:
        ProductRequestDto requestDto = new ProductRequestDtoBuilder().withStock(0).build(); //valor limite de 'stock' válido(0)
        Product mapped = new ProductBuilder().withId(null).build();

        Mockito.when(this.productMapper.toEntity(requestDto)).thenReturn(mapped);

        //Act:
        this.productService.create(requestDto);

        //Assert:
        Mockito.verify(productRepository).save(mapped);
    }


//update
    @Test
    void update_productIdInvalid_throwsAndDoesNotUpdate (){
        //Arrange:
        ProductRequestDto requestDto = Mockito.mock(ProductRequestDto.class);

        Mockito.when(this.productRepository.findById(999L)).thenReturn(Optional.empty());

        //Act:
        //Assert:
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> this.productService.update(999L, requestDto));

        Mockito.verify(this.productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_negativePrice_throwsAndDoesNotUpdate (){
        //Arrange:
        ProductRequestDto requestDto = new ProductRequestDtoBuilder().withPrice(new BigDecimal(-1)).build(); //price negativo
        Product searchedProduct = new ProductBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));

        //Act:
        Assertions.assertThrows(ProductPriceInvalidException.class,
                () -> this.productService.update(1L, requestDto));

        //Assert:
        Mockito.verify(this.productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_priceZero_throwsAndDoesNotUpdate (){
        //Arrange:
        ProductRequestDto requestDto = new ProductRequestDtoBuilder().withPrice(new BigDecimal(0)).build(); //price limite inválido
        Product searchedProduct = new ProductBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));

        //Act:
        Assertions.assertThrows(ProductPriceInvalidException.class,
                () -> this.productService.update(1L, requestDto));

        //Assert:
        Mockito.verify(this.productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_validLimitPrice_saveSuccessfully (){
        //Arrange:
        ProductRequestDto requestDto = new ProductRequestDtoBuilder().withPrice(new BigDecimal("0.01")).build(); //price limite válido
        Product searchedProduct = new ProductBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));

        //Act:
        this.productService.update(1L, requestDto);

        //Assert:
        Mockito.verify(this.productRepository).save(searchedProduct);
    }

    @Test
    void update_negativeStock_throwsAndDoesNotUpdate (){
        //Arrange:
        ProductRequestDto requestDto = new ProductRequestDtoBuilder().withStock(-1).build(); //stock limite inválido
        Product searchedProduct = new ProductBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));

        //Act:
        Assertions.assertThrows(ProductStockInvalidException.class,
                () -> this.productService.update(1L, requestDto));

        //Assert:
        Mockito.verify(this.productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_stockZero_saveSuccessfully (){
        //Arrange:
        ProductRequestDto requestDto = new ProductRequestDtoBuilder().withStock(0).build(); //stock limite válido
        Product searchedProduct = new ProductBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));

        //Act:
        this.productService.update(1L, requestDto);

        //Assert:
        Mockito.verify(this.productRepository).save(searchedProduct);
    }

    @Test
    void update_happyPath_updateFieldsSuccessfully (){
        //Arrange:
        ProductRequestDto requestDto = new ProductRequestDtoBuilder()
                .withName("Perfume")
                .withBrand("Avon")
                .withProductCategory(ProductCategory.FRAGRANCE)
                .withPrice(new BigDecimal("120.00"))
                .withStock(5).build();
        Product searchedProduct = new ProductBuilder().build();
        Product saved = new ProductBuilder().build();
        ProductResponseDto responseDto = new ProductResponseDtoBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.productRepository.save(searchedProduct)).thenReturn(saved);
        Mockito.when(this.productMapper.toResponseDto(saved)).thenReturn(responseDto);

        //Act:
        this.productService.update(1L, requestDto);

        //Assert:
        Mockito.verify(this.productRepository).save(this.productCaptor.capture());
        Product captured = this.productCaptor.getValue();

        Assertions.assertAll(
                () -> Assertions.assertEquals("Perfume", captured.getName()),
                () -> Assertions.assertEquals("Avon", captured.getBrand()),
                () -> Assertions.assertEquals(ProductCategory.FRAGRANCE, captured.getProductCategory()),
                () -> Assertions.assertEquals(new BigDecimal("120.00"), captured.getPrice()),
                () -> Assertions.assertEquals(5, captured.getStock())
        );
    }

    @Test
    void update_happyPath_productResponseExpected (){
        //Arrange:
        ProductRequestDto requestDto = new ProductRequestDtoBuilder()
                .withName("Perfume")
                .withBrand("Avon")
                .withProductCategory(ProductCategory.FRAGRANCE)
                .withPrice(new BigDecimal("120.00"))
                .withStock(5).build();
        Product searchedProduct = new ProductBuilder().build();
        Product saved = new ProductBuilder().build();
        ProductResponseDto responseDto = new ProductResponseDtoBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.productRepository.save(searchedProduct)).thenReturn(saved);
        Mockito.when(this.productMapper.toResponseDto(saved)).thenReturn(responseDto);

        //Act:
        ProductResponseDto returned = this.productService.update(1L, requestDto);

        //Assert:
        Mockito.verify(this.productRepository).save(searchedProduct);
        Assertions.assertSame(responseDto, returned);
    }



//addEntryToStock:
    @Test
    void addEntryToStock_productIdInvalid_throwsAndNotSave(){
        //Arrange:
        ProductEntryToStockRequestDto requestDto = Mockito.mock(ProductEntryToStockRequestDto.class);

        Mockito.when(this.productRepository.findById(999L)).thenReturn(Optional.empty());

        //Act:
        //Assert:
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> this.productService.addEntryToStock(999L, requestDto));

        Mockito.verify(this.productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void addEntryToStock_inputQuantityZero_throwsAndNotSave(){
        //Arrange:
        ProductEntryToStockRequestDto requestDto = new ProductEntryToStockRequestDto(0); //stock limite invalido '0'
        Product searchedProduct = new ProductBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));

        //Act:
        Assertions.assertThrows(InvalidStockEntryValueException.class,
                () -> this.productService.addEntryToStock(1L, requestDto));

        //Assert:
        Mockito.verify(this.productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void addEntryToStock_validLimitInputQuantity_saveSuccessfully (){
        //Arrange:
        ProductEntryToStockRequestDto requestDto = new ProductEntryToStockRequestDto(1); //stock limite válido
        Product searchedProduct = new ProductBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));

        //Act:
        this.productService.addEntryToStock(1L, requestDto);

        //Assert:
        Mockito.verify(this.productRepository).save(searchedProduct);
    }

    @Test
    void addEntryToStock_happyPath_updateFieldStockSuccessfully (){
        //Arrange:
        ProductEntryToStockRequestDto requestDto = new ProductEntryToStockRequestDto(10);
        Product searchedProduct = new ProductBuilder().withStock(5).build();
        Product saved = new ProductBuilder().build();
        ProductResponseDto responseDto = new ProductResponseDtoBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.productRepository.save(searchedProduct)).thenReturn(saved);
        Mockito.when(this.productMapper.toResponseDto(saved)).thenReturn(responseDto);

        //Act:
        this.productService.addEntryToStock(1L, requestDto);

        //Assert:
        Mockito.verify(this.productRepository).save(this.productCaptor.capture());

        Product captured = this.productCaptor.getValue();
       Assertions.assertEquals(15, captured.getStock());
    }

    @Test
    void addEntryToStock_happyPath_productResponseExpected (){
        //Arrange:
        ProductEntryToStockRequestDto requestDto = new ProductEntryToStockRequestDto(10);
        Product searchedProduct = new ProductBuilder().withStock(5).build();
        Product saved = new ProductBuilder().build();
        ProductResponseDto responseDto = new ProductResponseDtoBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.productRepository.save(searchedProduct)).thenReturn(saved);
        Mockito.when(this.productMapper.toResponseDto(saved)).thenReturn(responseDto);

        //Act:
        ProductResponseDto returned = this.productService.addEntryToStock(1L, requestDto);

        //Assert:
        Mockito.verify(this.productRepository).save(searchedProduct);
        Assertions.assertSame(responseDto, returned);
    }

    @Test
    void addEntryToStock_maximumInputQuantityAccepted_saveSuccessfully (){
        //Arrange:
        ProductEntryToStockRequestDto requestDto = new ProductEntryToStockRequestDto(50); //stock entry value maximum accepted
        Product searchedProduct = new ProductBuilder().withStock(5).build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));

        //Act:
        this.productService.addEntryToStock(1L, requestDto);

        //Assert:
        Mockito.verify(this.productRepository).save(searchedProduct);
    }

    @Test
    void addEntryToStock_inputQuantityMaximumAcceptedPlusOne_throwsAndNotSave (){
        //Arrange:
        ProductEntryToStockRequestDto requestDto = new ProductEntryToStockRequestDto(51); //stock entry value not accepted
        Product searchedProduct = new ProductBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));

        //Act:
        Assertions.assertThrows(StockEntryLimitExceededException.class,
                () -> this.productService.addEntryToStock(1L, requestDto));

        //Assert:
        Mockito.verify(this.productRepository, Mockito.never()).save(Mockito.any());
    }


//deleteById:
    @Test
    void deleteById_productIdInvalid_throwsAndNotDelete (){
        //Arrange
        Mockito.when(this.productRepository.findById(999L)).thenReturn(Optional.empty());

        //Act
        //Assert
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> this.productService.deleteById(999L));

        Mockito.verify(this.productRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void deleteById_productRelatedToASaleProduct_throwsAndNotDelete (){
        //Arrange
        Product searchedProduct = new ProductBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductRepository.existsByProductId(1L)).thenReturn(true);

        //Act
        //Assert
        Assertions.assertThrows(EntityInUseException.class,
                () -> this.productService.deleteById(1L));

        Mockito.verify(this.productRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void deleteById_happyPath_deleteSuccessfully (){
        //Arrange
        Product searchedProduct = new ProductBuilder().build();

        Mockito.when(this.productRepository.findById(1L)).thenReturn(Optional.of(searchedProduct));
        Mockito.when(this.saleProductRepository.existsByProductId(1L)).thenReturn(false);

        //Act
        this.productService.deleteById(1L);

        //Assert
        Mockito.verify(this.productRepository).delete(searchedProduct);
    }
}