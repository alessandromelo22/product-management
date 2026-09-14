package com.alessandromelo.repository;

import com.alessandromelo.entity.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {

    boolean existsByProductId(Long productId);
}
