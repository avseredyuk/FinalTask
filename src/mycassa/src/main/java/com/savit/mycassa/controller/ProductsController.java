package com.savit.mycassa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.savit.mycassa.service.UserService;

import lombok.AllArgsConstructor;

@Controller
@PreAuthorize("hasAnyAuthority('COMMODITY_EXPERT', 'CASHIER')")
@RequestMapping("/products")
@AllArgsConstructor
public class ProductsController {

	@Autowired
	private final UserService userService;

	@GetMapping("/")
	public String getProducts() {
		return "products";
	}


}
