package com.savit.mycassa.controller;


import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.savit.mycassa.dto.ProductData;
import com.savit.mycassa.dto.ProductsData;
import com.savit.mycassa.service.ProductService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
//@PreAuthorize("hasAnyAuthority('COMMODITY_EXPERT', 'CASHIER')")
@RequestMapping("/products")
@AllArgsConstructor
@Slf4j
public class ProductController {
	
	

	@Autowired
	private final ProductService productService;

	@GetMapping()
	public String getProducts(
								@RequestParam(required = false, defaultValue = "ean") String filterField,
			  					@RequestParam(required = false, defaultValue = "DESC") String direction,
			  					@RequestParam(required = false, defaultValue = "1") String page,
			  					@RequestParam(required = false, defaultValue = "10") String size,
			  					@RequestParam(required = false, defaultValue = "") String searchQuery,
			  					Model model) {
		
		log.info("[PAGINATION] Input params: filterField:[{}], direction:[{}], page:[{}], size:[{}], searchQuery:[{}]", 
				filterField, direction, page, size, searchQuery);
		model.addAttribute("productsData", productService.getAllProducts(filterField, direction, page, size, searchQuery));

		return "products";

	}
	

	@GetMapping("/new")
	public String getNewProductForm(@ModelAttribute ProductData productData,  Model model) {
		model.addAttribute("productData", productData);
		return "newProduct";
	}
	
	
	@PostMapping("/new")
	public String getNewProductForm(@Valid @ModelAttribute ProductData productData,
											BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error(" >> productData: {}", productData.toString());
			return "newProduct";
		}
		log.info(" >> productData: {}", productData.toString());
		
		productService.saveProduct(productData);

		return "redirect:/products";
	}
	
	@GetMapping("/sale/{ean}")
	public String sellProduct(@PathVariable String ean,  Model model) {
		
		model.addAttribute("productData", productService.getProductByEan(ean));
		
		return "addproduct";
	}


}
