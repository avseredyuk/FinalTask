package com.savit.mycassa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.savit.mycassa.entity.product.Product;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Long> {
	
	Page <Product> findAll(Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.ean LIKE CONCAT(CONCAT('%',?1),'%') OR p.title LIKE  CONCAT(CONCAT('%',?1),'%')")
	Page <Product> findByEanContainsIsOrTitleContainsIs(String searchQuery, Pageable pageable);

	Optional<Product> findByEan(String ean);

//	
//	select * from products 
//	inner join 
//	(select products.id as pid,  products.quantity_in_store-sum(quantity) as tempquantity from sales  
//			inner join products ON products.id = sales.product_id group by(products.id)) as d
//	on products.id = pid ;

	
}