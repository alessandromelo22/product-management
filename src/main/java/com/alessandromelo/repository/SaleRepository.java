package com.alessandromelo.repository;

import com.alessandromelo.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {

    //Verifica se existe alguma Venda vinculada a um Cliente especifico
    boolean existsByCustomerId(Long customerId);
}
