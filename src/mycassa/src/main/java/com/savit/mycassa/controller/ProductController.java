package com.savit.mycassa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.savit.mycassa.dto.ProductDTO;
import com.savit.mycassa.dto.SaleDTO;
import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.service.ProductService;
import com.savit.mycassa.service.SaleService;
import com.savit.mycassa.util.exception.CashierHasNotPermissionException;
import com.savit.mycassa.util.exception.ProductNotSavedException;

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


	@GetMapping
	public String getProductsPage(@RequestParam(required = false, defaultValue = "ean") String filterField,
			@RequestParam(required = false, defaultValue = "desc") String direction,
			@RequestParam(required = false, defaultValue = "1") String page,
			@RequestParam(required = false, defaultValue = "5") String size,
			@RequestParam(required = false, defaultValue = "") String searchQuery, Model model) {

		log.info("[PAGINATION] Input params: filterField:[{}], direction:[{}], page:[{}], size:[{}], searchQuery:[{}]",
				filterField, direction, page, size, searchQuery);
		model.addAttribute("products",
				productService.getAllProducts(filterField, direction, page, size, searchQuery));

		return "product/products";

	}

	@GetMapping("{ean}/edit")
	public String getUpdateProductPage(@PathVariable String ean, Model model) {

		ProductDTO productDTO = productService.getProductByEan(ean);

		model.addAttribute("product", productDTO);

		return "product/editProduct";
	}

	@PostMapping("{ean}/edit")
	public String updateProduct(@PathVariable String ean, @Valid @ModelAttribute("product") ProductDTO productDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasFieldErrors("quantityInStore") || 
				bindingResult.hasFieldErrors("price") || 
				bindingResult.hasFieldErrors("title")) {
			
				return "product/editProduct";
		}

		productService.updateProduct(productDTO, ean);

		return "redirect:/products";
	}

	@GetMapping("/new")
	public String getNewProductPage(Model model) {
		model.addAttribute("product", new ProductDTO());
		return "product/newProduct";
	}

	@PostMapping("/new")
	public String saveProduct(@Valid @ModelAttribute("product") ProductDTO productDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "product/newProduct";
		}
		try {
			productService.saveProduct(productDTO);			
		}catch(Exception ex) {
			throw new ProductNotSavedException();
		}
		return "redirect:/products";
	}

}
