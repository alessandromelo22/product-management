package com.alessandromelo.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository {

    //Verifica se existe alguma Venda vinculada a um Cliente especifico
    boolean existByCustomerId(Long customerId);
}
