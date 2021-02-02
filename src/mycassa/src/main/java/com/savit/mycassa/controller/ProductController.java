package com.savit.mycassa.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
	public String getProducts(@PageableDefault(sort = {"ean"}, direction = Sort.Direction.DESC, value = 5 ) 
									Pageable pageable, Model model) {
		
		
		ProductsData productsData = productService.getAllProducts(pageable);
//		productsData.getProductsData().hasContent()
		model.addAttribute("productsData", productsData);
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


}
