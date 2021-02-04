package com.savit.mycassa.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;

import com.savit.mycassa.dto.UserData;
import com.savit.mycassa.service.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
//@PreAuthorize("hasAnyAuthority('COMMODITY_EXPERT','SENIOR_CASHIER', 'CASHIER')")
public class UserController {

	@Autowired
	private final UserService userService;

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/profile")
	public String getUserProfile(Model model) {
		return "profile";
	}

	@GetMapping("/profile/edit")
	public String getUserEditForm(Model model) {
		UserData userData = userService.getPrincipal();

		model.addAttribute("userData", userData);
		log.info(" >> userData-input: {}", userData.toString());
		return "editUser";			
		
	}

	@PutMapping("/profile/edit")
	public String editUser(@Valid UserData userData, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			log.info(" >> userDTO-invalid: {}", userData.toString());
			return "editUser";
		}
		log.info(" >> userDTO-valid: {}", userData.toString());
		
		String currEmail = userService.getPrincipal().getEmail();
		
		//TODO: password confirmation
		//TODO: getting email from auth...ed user
		//TODO: put by email
		
		
		return "redirect:/profile";
	}

	

}

