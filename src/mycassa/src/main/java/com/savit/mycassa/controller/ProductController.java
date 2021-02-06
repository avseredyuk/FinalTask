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

import com.savit.mycassa.dto.ProductData;
import com.savit.mycassa.dto.SaleDTO;
import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.service.ProductService;
import com.savit.mycassa.service.SaleService;
import com.savit.mycassa.util.exception.CashierHasNotPermissionException;

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

	@Autowired
	private final SaleService saleService;

	@GetMapping()
	public String getProductsPage(@RequestParam(required = false, defaultValue = "ean") String filterField,
			@RequestParam(required = false, defaultValue = "desc") String direction,
			@RequestParam(required = false, defaultValue = "1") String page,
			@RequestParam(required = false, defaultValue = "5") String size,
			@RequestParam(required = false, defaultValue = "") String searchQuery, Model model) {

		log.info("[PAGINATION] Input params: filterField:[{}], direction:[{}], page:[{}], size:[{}], searchQuery:[{}]",
				filterField, direction, page, size, searchQuery);
		model.addAttribute("productsData",
				productService.getAllProducts(filterField, direction, page, size, searchQuery));

		return "products";

	}

	@GetMapping("/edit/{ean}")
	public String getUpdateProductPage(@PathVariable String ean, Model model) {

		ProductData productData = productService.getProductByEan(ean);

		model.addAttribute("productData", productData);

		return "editProduct";
	}

	@PostMapping("/edit/{ean}")
	public String updateProduct(@PathVariable String ean, @Valid @ModelAttribute ProductData productData,
			BindingResult bindingResult) {
		if (bindingResult.hasFieldErrors("quantityInStore") || bindingResult.hasFieldErrors("cost") || bindingResult.hasFieldErrors("title")) {
			return "editProduct";
		}

		productService.updateProduct(productData, ean);

		return "redirect:/products";
	}

	@GetMapping("/new")
	public String getNewProductPage(@ModelAttribute ProductData productData, Model model) {
		model.addAttribute("productData", productData);
		return "newProduct";
	}

	@PostMapping("/new")
	public String saveProduct(@Valid @ModelAttribute ProductData productData, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error(" >> productData: {}", productData.toString());
			return "newProduct";
		}
		log.info(" >> productData: {}", productData.toString());

		productService.saveProduct(productData);

		return "redirect:/products";
	}

	@GetMapping("/sale/{ean}")
	public String getSaleProductPage(@PathVariable String ean, SaleDTO saleDTO, Model model) {

		model.addAttribute("saleDTO", saleDTO);
		model.addAttribute("productData", productService.getProductByEan(ean));

		return "addproduct";
	}

	@PostMapping("/sale/{ean}")
	public String saleProduct(@PathVariable String ean, @Valid @ModelAttribute SaleDTO saleDTO,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "addproduct";
		}

		log.info("saleDTO: [{}], ean:{}", saleDTO, ean);
		
		try {
			Sale sale = saleService.newProductSale(ean, saleDTO);
		} catch (Exception ex) {
			throw new CashierHasNotPermissionException("{cashier.hasnt.add.product.to.check}");
		}
		return "redirect:/products";
	}

}
