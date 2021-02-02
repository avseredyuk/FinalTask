package com.savit.mycassa.service;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.savit.mycassa.dto.ProductData;
import com.savit.mycassa.dto.ProductsData;
import com.savit.mycassa.entity.product.Measure;
import com.savit.mycassa.entity.product.Product;
import com.savit.mycassa.repository.ProductRepository;
import com.savit.mycassa.util.Constants;
import com.savit.mycassa.util.Validator;

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
	
	
	private static Pageable buildPageable(String filterField, String direction, String pageS, String sizeS) {
		Validator.validatePageVariables(pageS, sizeS);
		Validator.validateFilterField(filterField);

		int page = Integer.parseInt(pageS);
		int size = Integer.parseInt(sizeS);
		
		Sort sort = "ASC".equals(direction) ? Sort.by(filterField).ascending() : 
				   ("DESC".equals(direction) ? Sort.by(filterField).descending() : null);
		
		Validator.validateDirection(sort);
		
		return PageRequest.of(page-1, size,  sort);
	}
	
	
	
	public ProductsData getAllProducts(String filterField, String direction, String pageS, String sizeS, String searchQuery) {
		//TODO: input params
		Pageable pageable = buildPageable(filterField, direction, pageS, sizeS);
		
		Page<Product> products = searchQuery.isBlank() ? 
				productRepository.findAll(pageable) : 	
				productRepository.findByEanContainsIsOrTitleContainsIs(searchQuery, pageable);


//		if(products.getSort().get().findFirst().isEmpty()) {
//			log.error("[PAGINATION ERROR] Page: {}, PerPage: {}", pageable.getPageNumber(), pageable.getPageSize());
//			throw new NoSuchElementException("{choose.the.params}");
//		}
		
		log.info("[PAGINATION] Page: [{}], PerPage: [{}], Sorted by field:[{}], Ordered by:[{}]", 
				pageable.getPageNumber(), pageable.getPageSize());
		pageable.getSort().get().forEach(a -> log.info("[PAGINATION] Sorted by field:{}, Ordered by:{}", a.getProperty(), a.getDirection().toString()));
		
		return new ProductsData(products, filterField,direction,searchQuery);
		
	}
	

}
