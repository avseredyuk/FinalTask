package com.savit.mycassa.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.savit.mycassa.dto.ProductDTO;
import com.savit.mycassa.dto.ProductsDTO;
import com.savit.mycassa.entity.product.Measure;
import com.savit.mycassa.entity.product.Product;
import com.savit.mycassa.repository.ProductRepository;
import com.savit.mycassa.util.exception.ProductNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class ProductService {

	@Autowired
	private final ProductRepository productRepository;


	@Transactional
	public void saveProduct(ProductDTO productDTO) {
		Product product = productRepository.save(new Product(productDTO.getTitle(), productDTO.getPrice(),
				productDTO.getQuantityInStore(), Measure.valueOf(productDTO.getMeasure())));
		product.setEan(String.format("%013d", product.getId()));
	}

	public ProductsDTO getAllProducts(String filterField, String direction, String pageS, String sizeS,
			String searchQuery) {
		
		Pageable pageable = buildPageable(filterField, direction, pageS, sizeS);

		Page<Product> products = searchQuery.isBlank() ? productRepository.findAll(pageable)
				: productRepository.findByEanContainsIsOrTitleContainsIs(searchQuery, pageable);

		return new ProductsDTO(products, filterField, direction, searchQuery);

	}

	public ProductDTO getProductByEan(String ean) {

		Product product = productRepository.findByEan(ean).orElseThrow(ProductNotFoundException::new);

		return ProductDTO.builder().title(product.getTitle()).ean(product.getEan())
				.price(product.getPrice()).quantityInStore(product.getQuantityInStore())
				.measure(product.getMeasure().name()).id(product.getId()).build();

	}
	

	public ProductDTO getProductById(Long id) {

		Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

		return ProductDTO.builder().title(product.getTitle()).ean(product.getEan())
				.price(product.getPrice()).quantityInStore(product.getQuantityInStore())
				.measure(product.getMeasure().name()).id(product.getId()).build();
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void updateProduct(ProductDTO productDTO, String ean) {
		Product product = productRepository.findByEan(ean).orElseThrow(ProductNotFoundException::new);
		
		product.setTitle(productDTO.getTitle());
		product.setPrice(productDTO.getPrice());
		product.setQuantityInStore(productDTO.getQuantityInStore());
	}
	

	private static Pageable buildPageable(String filterField, String direction, String pageS, String sizeS) {

		int page = Integer.parseInt(pageS);
		int size = Integer.parseInt(sizeS);

		Optional <Sort> sort = "asc".equals(direction) ? Optional.of(Sort.by(filterField).ascending())
				: ("desc".equals(direction) ? Optional.of(Sort.by(filterField).descending()) : Optional.of(null));

		
		return PageRequest.of(page - 1, size, sort.get());
	}


}
