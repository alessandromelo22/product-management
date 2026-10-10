package com.alessandromelo.repository;

import com.alessandromelo.entity.Sale;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {

    //Verifica se existe alguma Venda vinculada a um Cliente especifico
    boolean existsByCustomerId(Long customerId);

    @Override
    @EntityGraph(attributePaths = "saleProducts")// essa annotation permite que seja realizado um FETCH.EAGER para entidades secundarias especificas
    Optional<Sale> findById(Long saleId);

    //Retorna todos os registros entre um periodo especifico
    @EntityGraph(attributePaths = "saleProducts")
    List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);
}
