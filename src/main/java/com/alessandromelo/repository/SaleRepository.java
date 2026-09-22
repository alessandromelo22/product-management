package com.alessandromelo.repository;

import com.alessandromelo.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {

    //Verifica se existe alguma Venda vinculada a um Cliente especifico
    boolean existsByCustomerId(Long customerId);

    //Retorna todos os registros entre um periodo especifico
    List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);
}
