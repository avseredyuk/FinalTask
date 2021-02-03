package com.savit.mycassa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.savit.mycassa.entity.product.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

}
