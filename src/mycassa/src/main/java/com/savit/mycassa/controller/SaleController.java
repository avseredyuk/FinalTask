package com.savit.mycassa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.savit.mycassa.dto.SaleDTO;
import com.savit.mycassa.dto.SalesDTO;
import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.service.ProductService;
import com.savit.mycassa.service.SaleService;
import com.savit.mycassa.util.exception.CashierHasNotPermissionException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@AllArgsConstructor
public class SaleController {
	
	@Autowired
	private final SaleService saleService;
	
	@Autowired
	private final ProductService productService;
	
	
	
	
	
	
	@GetMapping("sales/check/overview")
	public String getSalesList(Model model){
		
		SalesDTO salesDTO =  saleService.getAllSessionSales();
		
		model.addAttribute("salesDTO", salesDTO);
		
		return "checkOverview";
	}
	
	
	
	@GetMapping("/sales/new/{ean}")
	public String getSaleProductPage(@PathVariable String ean, SaleDTO saleDTO, Model model) {

		model.addAttribute("saleDTO", saleDTO);
		model.addAttribute("productData", productService.getProductByEan(ean));

		return "addproduct";
	}

	@PostMapping("/sales/new/{ean}")
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
	
	
	@RequestMapping(value = "/sales/check/print", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource>  getSalesReport (Model model){
		
		SalesDTO salesDTO =  saleService.getAllSessionSales();
		

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=check.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(saleService.getCheck()));
	}

	
	
	
	
	

}
