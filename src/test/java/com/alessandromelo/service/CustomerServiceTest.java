package com.alessandromelo.service;

import com.alessandromelo.builders.customer.CustomerBuilder;
import com.alessandromelo.builders.customer.CustomerRequestDtoBuilder;
import com.alessandromelo.builders.customer.CustomerResponseDtoBuilder;
import com.alessandromelo.dto.customer.CustomerRequestDto;
import com.alessandromelo.dto.customer.CustomerResponseDto;
import com.alessandromelo.entity.Customer;
import com.alessandromelo.exception.customer.CpfAlreadyExistsException;
import com.alessandromelo.exception.customer.CustomerNotFoundException;
import com.alessandromelo.exception.customer.PhoneNumberAlreadyExistsException;
import com.alessandromelo.exception.global.EntityInUseException;
import com.alessandromelo.mapper.CustomerMapper;
import com.alessandromelo.repository.CustomerRepository;
import com.alessandromelo.repository.SaleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private SaleRepository saleRepository;
    @Captor
    private ArgumentCaptor<Customer> customerCaptor;



    /**<p><b>getAll():</b></p>
    *
    *  <p>1- It should return an empty list.</p>
    *  <p>2- It should return a list of CustomerResponseDto</p>
    */
    @Test
    @DisplayName("getAll() should return an empty list.")
    void getAllShouldReturnAnEmptyList() {
        //Arrange:
        when(this.customerRepository.findAll()).thenReturn(List.of());

        //Act:
        List<CustomerResponseDto> returned = this.customerService.getAll();

        //Assert:
        Assertions.assertNotNull(returned);
        Assertions.assertTrue(returned.isEmpty());
    }


    @Test
    @DisplayName("getAll() should return a list of CustomerResponseDto")
    void getAllShouldReturnAListOfCustomerResponseDto() {
        //Arrange:
        Customer customer = new CustomerBuilder().build();
        CustomerResponseDto responseDTO = new CustomerResponseDtoBuilder().build();

        when(this.customerRepository.findAll()).thenReturn(List.of(customer));
        when(this.customerMapper.toResponseDto(customer)).thenReturn(responseDTO);

        //Act:
        List<CustomerResponseDto> returned = this.customerService.getAll();

        //Assert:
        Assertions.assertNotNull(returned);
        Assertions.assertEquals(1, returned.size());

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, returned.get(0).getId()),
                () -> Assertions.assertEquals("João da Silva", returned.get(0).getName()),
                () -> Assertions.assertEquals("37123456789", returned.get(0).getPhoneNumber())
        );

        verify(this.customerMapper, times(1)).toResponseDto(customer);
    }



    /**<p><b>getById():</b></p>
     *
     *  <p>1- It should throw CustomerNotFoundException</p>
     *  <p>2- It should return a CustomerResponseDto</p>
     */
    @Test
    @DisplayName("getById() should throw CustomerNotFoundException")
    void getByIdShouldThrowCustomerNotFoundException(){
        //Arrange:
        when(this.customerRepository.findById(999L)).thenReturn(Optional.empty());

        //Act:
        //Assert:
        Assertions.assertThrows(CustomerNotFoundException.class,
                () -> this.customerService.getById(999L));

        verify(this.customerMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("getById() should return a CustomerResponseDto")
    void getByIdShouldReturnACustomerResponseDto() {
        //Arrange:
        Customer customer = new CustomerBuilder().build();
        CustomerResponseDto responseDTO = new CustomerResponseDtoBuilder().build();

        when(this.customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(this.customerMapper.toResponseDto(customer)).thenReturn(responseDTO);

        //Act:
        CustomerResponseDto returned = this.customerService.getById(1L);

        //Assert:
        Assertions.assertNotNull(returned);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, returned.getId()),
                () -> Assertions.assertEquals("João da Silva", returned.getName()),
                () -> Assertions.assertEquals("37123456789", returned.getPhoneNumber())
        );
        verify(this.customerMapper).toResponseDto(customer);
    }



    /**<p><b>create():</b></p>
     *
     *  <p>1- It should throw PhoneNumberAlreadyExistsException</p>
     *  <p>2- It should throw CpfAlreadyExistsException</p>
     *  <p>3- It should return a CustomerResponseDto</p>
     */
    @Test
    @DisplayName("create() should throw PhoneNumberAlreadyExistsException")
    void createShouldThrowPhoneNumberAlreadyExistsException(){
        //Arrange:
        CustomerRequestDto requestDto = new CustomerRequestDtoBuilder().build();

        when(this.customerRepository.existsByCpf(requestDto.getCpf())).thenReturn(false);
        when(this.customerRepository.existsByPhoneNumber(requestDto.getPhoneNumber())).thenReturn(true);

        //Act:
        //Assert:
        Assertions.assertThrows(PhoneNumberAlreadyExistsException.class,
                () -> this.customerService.create(requestDto));

        verify(this.customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("create() should throw CpfAlreadyExistsException")
    void createShouldThrowCpfAlreadyExistsException(){
        //Arrange:
        CustomerRequestDto requestDto = new CustomerRequestDtoBuilder().build();

        when(this.customerRepository.existsByCpf(requestDto.getCpf())).thenReturn(true);

        //Act:
        //Assert:
        Assertions.assertThrows(CpfAlreadyExistsException.class,
                () -> this.customerService.create(requestDto));

        verify(this.customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("create() should return a CustomerResponseDto")
    void createShouldReturnACustomerResponseDto() {
        //Arrange:
        CustomerRequestDto requestDto = new CustomerRequestDtoBuilder().build();
        Customer customer = new CustomerBuilder().build();
        CustomerResponseDto responseDTO = new CustomerResponseDtoBuilder().build();

        when(this.customerRepository.existsByCpf(requestDto.getCpf())).thenReturn(false);
        when(this.customerRepository.existsByPhoneNumber(requestDto.getPhoneNumber())).thenReturn(false);
        when(this.customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(this.customerRepository.save(customer)).thenReturn(customer);
        when(this.customerMapper.toResponseDto(customer)).thenReturn(responseDTO);

        //Act:
        CustomerResponseDto returned = this.customerService.create(requestDto);

        //Assert:
        Assertions.assertNotNull(returned);

        verify(this.customerRepository).save(this.customerCaptor.capture());
        Customer captured = this.customerCaptor.getValue();
        Assertions.assertAll(
                () -> Assertions.assertEquals("João da Silva", captured.getName()),
                () -> Assertions.assertEquals("37123456789", captured.getPhoneNumber()),
                () -> Assertions.assertEquals("067.854.802-44", captured.getCpf())
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, returned.getId()),
                () -> Assertions.assertEquals("João da Silva", returned.getName()),
                () -> Assertions.assertEquals("37123456789", returned.getPhoneNumber())
        );
        verify(this.customerRepository, times(1)).save(customer);
    }



    /**<p><b>update():</b></p>
     *
     *  <p>1- It should throw CustomerNotFoundException</p>
     *  <p>2- It should throw PhoneNumberAlreadyExistsException</p>
     *  <p>3- It should throw CpfAlreadyExistsException</p>
     *  <p>4- It should return a CustomerResponseDto</p>
     */
    @Test
    @DisplayName("update() should throw CustomerNotFoundException")
    void updateShouldThrowCustomerNotFoundException(){
        //Arrange:
        CustomerRequestDto requestDto = new CustomerRequestDtoBuilder()
                .withName("Maria")
                .withPhoneNumber("085769945")
                .withCpf("976.566.435-67")
                .build();

        when(this.customerRepository.findById(999L)).thenReturn(Optional.empty());

        //Act:
        //Assert:
        Assertions.assertThrows(CustomerNotFoundException.class,
                () -> this.customerService.update(999L, requestDto));

        verify(this.customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("update() should throw PhoneNumberAlreadyExistsException")
    void updateShouldThrowPhoneNumberAlreadyExistsException(){
        //Arrange:
        CustomerRequestDto requestDto = new CustomerRequestDtoBuilder()
                .withName("Maria")
                .withPhoneNumber("085769945")
                .withCpf("976.566.435-67")
                .build();

        Customer customer = new CustomerBuilder().build();

        when(this.customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(this.customerRepository.existsByPhoneNumber(requestDto.getPhoneNumber())).thenReturn(true);

        //Act:
        //Assert:
        Assertions.assertThrows(PhoneNumberAlreadyExistsException.class,
                () -> this.customerService.update(1L, requestDto));

        verify(this.customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("update() should throw CpfAlreadyExistsException")
    void updateShouldThrowCpfAlreadyExistsException(){
        //Arrange:
        CustomerRequestDto requestDto = new CustomerRequestDtoBuilder()
                .withName("Maria")
                .withPhoneNumber("085769945")
                .withCpf("976.566.435-67")
                .build();

        Customer customer = new CustomerBuilder().build();

        when(this.customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(this.customerRepository.existsByPhoneNumber(requestDto.getPhoneNumber())).thenReturn(false);
        when(this.customerRepository.existsByCpf(requestDto.getCpf())).thenReturn(true);

        //Act:
        //Assert:
        Assertions.assertThrows(CpfAlreadyExistsException.class,
                () -> this.customerService.update(1L, requestDto));

        verify(this.customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("update() should return a CustomerResponseDto")
    void updateShouldReturnACustomerResponseDto(){
        //Arrange:
        CustomerRequestDto requestDto = new CustomerRequestDtoBuilder()
                .withName("Maria")
                .withPhoneNumber("085769945")
                .withCpf("976.566.435-67")
                .build();

        Customer customer = new CustomerBuilder().build();

        CustomerResponseDto responseDTO = new CustomerResponseDtoBuilder()
                .withId(1L)
                .withName("Maria")
                .withPhoneNumber("085769945")
                .build();

        when(this.customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(this.customerRepository.existsByPhoneNumber(requestDto.getPhoneNumber())).thenReturn(false);
        when(this.customerRepository.existsByCpf(requestDto.getCpf())).thenReturn(false);
        when(this.customerRepository.save(customer)).thenReturn(customer);
        when(this.customerMapper.toResponseDto(customer)).thenReturn(responseDTO);

        //Act:
        CustomerResponseDto returned = this.customerService.update(1L, requestDto);

        //Assert:
        Assertions.assertNotNull(returned);

        verify(this.customerRepository).save(this.customerCaptor.capture());
        Customer captured = this.customerCaptor.getValue();
        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, captured.getId()),
                () -> Assertions.assertEquals("Maria", captured.getName()),
                () -> Assertions.assertEquals("085769945", captured.getPhoneNumber()),
                () -> Assertions.assertEquals("976.566.435-67", captured.getCpf())
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, returned.getId()),
                () -> Assertions.assertEquals("Maria", returned.getName()),
                () -> Assertions.assertEquals("085769945", returned.getPhoneNumber())
        );

        verify(this.customerRepository, times(1)).save(customer);
    }



    /**<p><b>deleteById():</b></p>
     *
     *  <p>1- It should throw CustomerNotFoundException</p>
     *  <p>2- It should throw EntityInUseException</p>
     *  <p>3- It should call the delete() method</p>
     */
    @Test
    @DisplayName("deleteById() should throw CustomerNotFoundException")
    void deleteByIdShouldThrowCustomerNotFoundException(){
        //Arrange:
        when(this.customerRepository.findById(999L)).thenReturn(Optional.empty());

        //Act:
        //Assert:
        Assertions.assertThrows(CustomerNotFoundException.class,
                () -> this.customerService.deleteById(999L));

        verify(this.customerRepository, never()).delete(any());
    }

    @Test
    @DisplayName("deleteById() should throw EntityInUseException")
    void deleteByIdShouldThrowEntityInUseException(){
        //Arrange:
        Customer customer = new CustomerBuilder().build();

        when(this.customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(this.saleRepository.existByCustomerId(1L)).thenReturn(true);

        //Act:
        //Assert:
        Assertions.assertThrows(EntityInUseException.class,
                () -> this.customerService.deleteById(1L));

        verify(this.customerRepository, never()).delete(any());
    }

    @Test
    @DisplayName("deleteById() should call the delete() method")
    void deleteByIdShouldCallTheDeleteMethod(){
        //Arrange:
        Customer customer = new CustomerBuilder().build();

        when(this.customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(this.saleRepository.existByCustomerId(1L)).thenReturn(false);

        //Act:
        this.customerService.deleteById(1L);

        //Assert:
        verify(this.customerRepository, times(1)).delete(customer);
    }
}