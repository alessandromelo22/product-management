package com.alessandromelo.repository;

import com.alessandromelo.builders.customer.CustomerBuilder;
import com.alessandromelo.entity.Customer;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;


@DataJpaTest
class CustomerRepositoryTest {

    private final CustomerRepository customerRepository;
    private final TestEntityManager testEntityManager;

    @Autowired
    public CustomerRepositoryTest(CustomerRepository customerRepository, TestEntityManager testEntityManager) {
        this.customerRepository = customerRepository;
        this.testEntityManager = testEntityManager;
    }


    @Test
    @DisplayName("existsByPhoneNumber() should return True")
    void existsByPhoneNumberShouldReturnTrue(){
        //Arrange
        Customer customer = new Customer();
        customer.setName("Maria");
        customer.setPhoneNumber("123456789");
        customer.setCpf("11111111111");
        this.testEntityManager.persistAndFlush(customer);

        //Act
        boolean returned = this.customerRepository.existsByPhoneNumber("123456789");

        //Assert
        Assertions.assertTrue(returned);
    }

    @Test
    @DisplayName("existsByPhoneNumber() should return False")
    void existsByPhoneNumberShouldReturnFalse(){
        //Arrange
        Customer customer = new Customer();
        customer.setName("Maria");
        customer.setPhoneNumber("123456789");
        customer.setCpf("11111111111");
        this.testEntityManager.persistAndFlush(customer);

        //Act
        boolean returned = this.customerRepository.existsByPhoneNumber("776855534");

        //Assert
        Assertions.assertFalse(returned);
    }


    @Test
    @DisplayName("existsByCpf() should return True")
    void existsByCpfShouldReturnTrue(){
        //Arrange
        Customer customer = new Customer();
        customer.setName("Maria");
        customer.setPhoneNumber("123456789");
        customer.setCpf("11111111111");
        this.testEntityManager.persistAndFlush(customer);

        //Act
        boolean returned = this.customerRepository.existsByCpf("11111111111");

        //Assert
        Assertions.assertTrue(returned);
    }

    @Test
    @DisplayName("existsByCpf() should return False")
    void existsByCpfShouldReturnFalse(){
        //Arrange
        Customer customer = new Customer();
        customer.setName("Maria");
        customer.setPhoneNumber("123456789");
        customer.setCpf("11111111111");
        this.testEntityManager.persistAndFlush(customer);

        //Act
        boolean returned = this.customerRepository.existsByCpf("99999999999");

        //Assert
        Assertions.assertFalse(returned);
    }



    //Constraints:
    @Test
    @DisplayName("should not save when have a duplicate phoneNumber")
    void shouldNotSaveWhenHaveADuplicatePhoneNumber(){
        //Arrange:
        Customer c1 = new Customer();
        c1.setName("Maria");
        c1.setPhoneNumber("123456789");
        c1.setCpf("067.854.802-44");
        this.testEntityManager.persistAndFlush(c1);

        Customer c2 = new Customer();
        c2.setName("Jose");
        c2.setPhoneNumber("123456789");
        c2.setCpf("912.485.351-87");

        //Act:
        //Assert:
        Assertions.assertThrows(DataIntegrityViolationException.class,
                ()-> this.customerRepository.save(c2));
    }
    
    @Test
    @DisplayName("should not save when have a duplicate cpf")
    void shouldNotSaveWhenHaveADuplicateCpf(){
        //Arrange:
        Customer c1 = new Customer();
        c1.setName("Maria");
        c1.setPhoneNumber("123456789");
        c1.setCpf("067.854.802-44");
        this.testEntityManager.persistAndFlush(c1);

        Customer c2 = new Customer();
        c2.setName("Jose");
        c2.setPhoneNumber("998765512");
        c2.setCpf("067.854.802-44");

        //Act:
        //Assert:
        Assertions.assertThrows(DataIntegrityViolationException.class,
                ()-> this.customerRepository.save(c2));
    }

    @Test
    @DisplayName("It should not save when the phoneNumber is null")
    void shouldNotSaveWhenThePhoneNumberIsNull(){
        //Arrange:
        Customer c1 = new Customer();
        c1.setName("Maria");
        c1.setCpf("067.854.802-44");

        //Act:
        //Assert:
        Assertions.assertThrows(DataIntegrityViolationException.class,
                ()-> this.customerRepository.save(c1));
    }

    @Test
    @DisplayName("It should not save when the cpf is null")
    void shouldNotSaveWhenTheCpfIsNull(){
        //Arrange:
        Customer c1 = new Customer();
        c1.setName("Maria");
        c1.setPhoneNumber("123456789");

        //Act:
        //Assert:
        Assertions.assertThrows(DataIntegrityViolationException.class,
                ()-> this.customerRepository.save(c1));
    }
}