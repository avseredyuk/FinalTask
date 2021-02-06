package com.savit.mycassa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.savit.mycassa.dto.SalesDTO;
import com.savit.mycassa.service.SaleService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@AllArgsConstructor
public class SaleController {
	
	@Autowired
	private final SaleService saleService;
	
	@GetMapping("/check/overview")
	public String getSalesList(Model model){
		
		SalesDTO salesDTO =  saleService.getAllSessionSales();
		
		model.addAttribute("salesDTO", salesDTO);
		
		return "checkOverview";
	}
	
	
//	@GetMapping("/check/print")
//	public String (Model model){
//		
//		SalesDTO salesDTO =  saleService.getAllSessionSales();
//		
//		model.addAttribute("salesDTO", salesDTO);
//		
//		return "checkOverview";
//	}

	
	
	

}
