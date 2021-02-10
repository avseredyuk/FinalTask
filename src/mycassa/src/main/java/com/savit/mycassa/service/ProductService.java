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

	//TODO Why& @T ransactional
	@Transactional
	public void saveProduct(ProductDTO productDTO) {

		productRepository.save(new Product(productDTO.getTitle(), productDTO.getPrice(),
				productDTO.getQuantityInStore(), Measure.valueOf(productDTO.getMeasure())));
	}
//TODO:searchQuery=
	
	public ProductsDTO getAllProducts(String filterField, String direction, String pageS, String sizeS,
			String searchQuery) {
		Pageable pageable = buildPageable(filterField, direction, pageS, sizeS);

		Page<Product> products = searchQuery.isBlank() ? productRepository.findAll(pageable)
				: productRepository.findByEanContainsIsOrTitleContainsIs(searchQuery, pageable);

//		log.info("[PAGINATION] Page: [{}], PerPage: [{}], Sorted by field:[{}], Ordered by:[{}]",
//				pageable.getPageNumber(), pageable.getPageSize());
//		pageable.getSort().get().forEach(a -> log.info("[PAGINATION] Sorted by field:{}, Ordered by:{}",
//				a.getProperty(), a.getDirection().toString()));

		return new ProductsDTO(products, filterField, direction, searchQuery);

	}

	public ProductDTO getProductByEan(String ean) {

		Product product = productRepository.findByEan(ean).orElseThrow(() -> new ProductNotFoundException("Such product not exists"));

		return ProductDTO.builder().title(product.getTitle()).ean(product.getEan())
				.price(product.getPrice()).quantityInStore(product.getQuantityInStore())
				.measure(product.getMeasure().name()).id(product.getId()).build();

	}
	

	public ProductDTO getProductById(Long id) {

		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Such product not exists"));

		return ProductDTO.builder().title(product.getTitle()).ean(product.getEan())
				.price(product.getPrice()).quantityInStore(product.getQuantityInStore())
				.measure(product.getMeasure().name()).id(product.getId()).build();
	}
	
	//TODO 	propagation=Propagation.REQUIRED - what is it?
	@Transactional(	propagation=Propagation.REQUIRED, rollbackFor = {Exception.class})
	public void updateProduct(ProductDTO productDTO, String ean) {
		Product product = productRepository.findByEan(ean).orElseThrow(() -> new ProductNotFoundException("Such product not exists"));

		
		product.setTitle(productDTO.getTitle());
		product.setPrice(productDTO.getPrice());
		product.setQuantityInStore(productDTO.getQuantityInStore());
		//XXX: need to save?
//		productRepository.save(product.setCost(productData.getCost()).quantityInStore(productData.getQuantityInStore()).build());
	}
	

	private static Pageable buildPageable(String filterField, String direction, String pageS, String sizeS) {

		int page = Integer.parseInt(pageS);
		int size = Integer.parseInt(sizeS);

		Optional <Sort> sort = "asc".equals(direction) ? Optional.of(Sort.by(filterField).ascending())
				: ("desc".equals(direction) ? Optional.of(Sort.by(filterField).descending()) : Optional.of(null));

		
		return PageRequest.of(page - 1, size, sort.get());
	}


}
