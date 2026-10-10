package com.alessandromelo.repository;

import com.alessandromelo.entity.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {

    List<SaleProduct> findBySaleId(Long saleId);

    boolean existsByProductId(Long productId);

    boolean existsBySaleId(Long saleId);
}
