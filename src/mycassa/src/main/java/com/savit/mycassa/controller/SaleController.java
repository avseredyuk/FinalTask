package com.savit.mycassa.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.savit.mycassa.dto.SaleDTO;
import com.savit.mycassa.service.ProductService;
import com.savit.mycassa.service.SaleService;
import com.savit.mycassa.util.exception.CashierHasNotPermissionException;
import com.savit.mycassa.util.exception.ProductNotFoundException;
import com.savit.mycassa.util.exception.SessionNotStartedYetException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/sales")
public class SaleController {
	
	@Autowired
	private final SaleService saleService;
	
	@Autowired
	private final ProductService productService;

	
	@PreAuthorize("hasAuthority('CASHIER')")
	@GetMapping("{ean}/new")
	public String getSaleProductPage(@PathVariable String ean, 
			Model model) {
		model.addAttribute("sale", new SaleDTO());
		model.addAttribute("product", productService.getProductByEan(ean));

		return "sale/newSale";
	}

	@PostMapping("{ean}/new")
	public String saleProduct(@PathVariable String ean, @Valid @ModelAttribute("sale") SaleDTO saleDTO,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "sale/newSale";
		}
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			saleService.newProductSaleAuth(ean, saleDTO, userDetails);			
		}catch(SessionNotStartedYetException ex) {
			throw new SessionNotStartedYetException();
		}catch(ProductNotFoundException ex) {
			throw new ProductNotFoundException();
		}catch(Exception ex) {
			throw new CashierHasNotPermissionException(ex.getMessage(), ean);
		}
		return "redirect:/products";
	}
	
	@GetMapping("{saleId}/delete")
	public String deleteSaleInCheck(@PathVariable("saleId") Long saleId,
			Model model) {

		log.info("START DELETING SALE");

		saleService.deleteSaleFromCheck(saleId);

		return "redirect:/session/requests";
	}

	
	

}
