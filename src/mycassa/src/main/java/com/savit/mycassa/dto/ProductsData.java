package com.savit.mycassa.dto;

import org.springframework.data.domain.Page;

import com.savit.mycassa.entity.product.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductsData {
	private Page<Product> productsData;
	
	private String filterField;
	
	private String direction;
	
	private String searchQuery;
}
