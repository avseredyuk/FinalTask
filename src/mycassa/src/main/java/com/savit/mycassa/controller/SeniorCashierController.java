package com.savit.mycassa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.savit.mycassa.dto.UsersData;
import com.savit.mycassa.service.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class SeniorCashierController {
	
	private static final Logger log = LoggerFactory.getLogger(SeniorCashierController.class);

	
	@Autowired
	private final UserService userService;
	
	@GetMapping("/cashiers")
	public String getAllCashiers(@ModelAttribute UsersData usersData, Model model){
		usersData = userService.getAllUsersCashiers();
		model.addAttribute("usersData", usersData);
		return "cashiers";
	}

}
