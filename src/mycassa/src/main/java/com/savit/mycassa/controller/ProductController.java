package com.savit.mycassa.controller;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.savit.mycassa.dto.ProductData;
import com.savit.mycassa.dto.ProductsData;
import com.savit.mycassa.service.ProductService;

import lombok.AllArgsConstructor;

@Controller
@PreAuthorize("hasAnyAuthority('COMMODITY_EXPERT', 'CASHIER')")

@AllArgsConstructor
public class ProductController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private final ProductService productService;

	@GetMapping("/products")
	public String getProducts(@ModelAttribute ProductsData productsData,  Model model) {
		productsData = productService.getAllProducts();
		model.addAttribute("productsData", productsData);
		return "products";
	}
	
	@GetMapping("/products/new")
	public String getNewProductForm(@ModelAttribute ProductData productData,  Model model) {
		model.addAttribute("productData", productData);
		return "products";
	}
	
	
	@PostMapping("/products/new")
	public String getNewProductForm(@Valid @ModelAttribute ProductData productData,
											BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error(" >> productData: {}", productData.toString());
			return "/new";
		}
		log.info(" >> productData: {}", productData.toString());
		
		productService.saveProduct(productData);

		return "redirect:/products";
	}


}
