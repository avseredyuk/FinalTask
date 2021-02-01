package com.savit.mycassa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.savit.mycassa.dto.ProductData;
import com.savit.mycassa.dto.ProductsData;
import com.savit.mycassa.entity.product.Measure;
import com.savit.mycassa.entity.product.Product;
import com.savit.mycassa.repository.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProductService {
	
	@Autowired
	private final ProductRepository productRepository;
	
	
	public void saveProduct(ProductData productData) {
		
		productRepository.save(
				new Product(productData.getTitle(), productData.getCost(), 
						productData.getQuantityInStore(), Measure.valueOf(productData.getMeasure()))
		);
	}
	
	public ProductsData getAllProducts() {
		
		return  new ProductsData(productRepository.findAll());
		
	}
	

}
