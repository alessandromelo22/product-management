package com.alessandromelo.repository;

import com.alessandromelo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    //Query Methods:

    boolean existsByCpf(String cpf);
    boolean existsByPhoneNumber(String phoneNumber);
}
