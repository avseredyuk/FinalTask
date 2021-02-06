package com.savit.mycassa.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.savit.mycassa.dto.ProductData;
import com.savit.mycassa.dto.ProductsData;
import com.savit.mycassa.entity.product.Measure;
import com.savit.mycassa.entity.product.Product;
import com.savit.mycassa.repository.ProductRepository;
import com.savit.mycassa.util.Validator;
import com.savit.mycassa.util.exception.ProductNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class ProductService {

	@Autowired
	private final ProductRepository productRepository;

	public void saveProduct(ProductData productData) {

		productRepository.save(new Product(productData.getTitle(), productData.getCost(),
				productData.getQuantityInStore(), Measure.valueOf(productData.getMeasure())));
	}

	public ProductsData getAllProducts(String filterField, String direction, String pageS, String sizeS,
			String searchQuery) {
		Pageable pageable = buildPageable(filterField, direction, pageS, sizeS);

		Page<Product> products = searchQuery.isBlank() ? productRepository.findAll(pageable)
				: productRepository.findByEanContainsIsOrTitleContainsIs(searchQuery, pageable);

//		log.info("[PAGINATION] Page: [{}], PerPage: [{}], Sorted by field:[{}], Ordered by:[{}]",
//				pageable.getPageNumber(), pageable.getPageSize());
//		pageable.getSort().get().forEach(a -> log.info("[PAGINATION] Sorted by field:{}, Ordered by:{}",
//				a.getProperty(), a.getDirection().toString()));

		return new ProductsData(products, filterField, direction, searchQuery);

	}

	public ProductData getProductByEan(String ean) {

		Optional<Product> product = productRepository.findByEan(ean);

		if (product.isEmpty()) {
			throw new ProductNotFoundException(String.format("No product with ean %s", ean));
		}

		return ProductData.builder().title(product.get().getTitle()).ean(product.get().getEan())
				.cost(product.get().getCost()).quantityInStore(product.get().getQuantityInStore())
				.measure(product.get().getMeasure().name()).id(product.get().getId()).build();

	}
	

	public ProductData getProductById(Long id) {
		Optional<Product> product = productRepository.findById(id);

		if (product.isEmpty()) {
			throw new NoSuchElementException(String.format("No product with id %d", id));
		}

		return ProductData.builder().title(product.get().getTitle()).ean(product.get().getEan())
				.cost(product.get().getCost()).quantityInStore(product.get().getQuantityInStore())
				.measure(product.get().getMeasure().name()).id(product.get().getId()).build();
	}
	
	@Transactional(	propagation=Propagation.REQUIRED, rollbackFor = {Exception.class})
	public void updateProduct(ProductData productData, String ean) {
		Product product = productRepository.findByEan(ean).get();
		log.info("product: {}", product);
		product.setTitle(productData.getTitle());
		product.setCost(productData.getCost());
		product.setQuantityInStore(productData.getQuantityInStore());
//		productRepository.save(product.setCost(productData.getCost()).quantityInStore(productData.getQuantityInStore()).build());
	}
	

	private static Pageable buildPageable(String filterField, String direction, String pageS, String sizeS) {
		Validator.validatePageVariables(pageS, sizeS);
		Validator.validateFilterField(filterField);

		int page = Integer.parseInt(pageS);
		int size = Integer.parseInt(sizeS);

		Sort sort = "asc".equals(direction) ? Sort.by(filterField).ascending()
				: ("desc".equals(direction) ? Sort.by(filterField).descending() : null);

		Validator.validateDirection(sort);

		return PageRequest.of(page - 1, size, sort);
	}


}
