package com.alessandromelo.repository;

import com.alessandromelo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    //Query Methods:

    boolean existsByCpf(String cpf);
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByCpfAndIdNot(String cpf, Long id);
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);
    //Trocar dentro do Service PUT e dos testes tambem;
}
