package com.savit.mycassa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.savit.mycassa.controller.ProductController;
import com.savit.mycassa.dto.ProductData;
import com.savit.mycassa.dto.ProductsData;
import com.savit.mycassa.entity.product.Measure;
import com.savit.mycassa.entity.product.Product;
import com.savit.mycassa.repository.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class ProductService {
	
	@Autowired
	private final ProductRepository productRepository;
	
	
	public void saveProduct(ProductData productData) {
		
		productRepository.save(
				new Product(productData.getTitle(), productData.getCost(), 
						productData.getQuantityInStore(), Measure.valueOf(productData.getMeasure()))
		);
	}
	
	public ProductsData getAllProducts(Pageable pageable) {
		log.info("[PAGINATION] Page: {}, PerPage: {}", pageable.getPageNumber(), pageable.getPageSize());
		pageable.getSort().get().forEach(a -> log.info("[PAGINATION] Sorted by field:{}, Ordered by:{}", a.getProperty(), a.getDirection().toString()));
		
		return new ProductsData(productRepository.findAll(pageable));
		
	}
	

}
