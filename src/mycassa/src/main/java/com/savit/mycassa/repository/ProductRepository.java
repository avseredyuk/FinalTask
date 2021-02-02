package com.savit.mycassa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.savit.mycassa.entity.product.Product;

public interface ProductRepository  extends JpaRepository<Product, Long> {
	
	Page <Product> findAll(Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.ean LIKE CONCAT(CONCAT('%',?1),'%') OR p.title LIKE  CONCAT(CONCAT('%',?1),'%')")
	Page <Product> findByEanContainsIsOrTitleContainsIs(String searchQuery, Pageable pageable);
}