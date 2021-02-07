package com.savit.mycassa.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import com.savit.mycassa.dto.UserData;
import com.savit.mycassa.service.UserService;
import com.savit.mycassa.util.exception.EmailExistsException;

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
		
		model.addAttribute("userData", userService.getPrincipalUserDTO());
		return "profile";
	}

	@GetMapping("/profile/edit")
	public String getUserEditForm(Model model) {
		UserData userData = userService.getPrincipalUserDTO(); //TODO: refactor Data to DTO
		model.addAttribute("userData", userData); //TODO Refactor userData to user

		return "editUser";			
		
	}

	@PostMapping("/profile/edit")
	public String editUser(@Valid UserData userData,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			if(bindingResult.getAllErrors().size() > 1) {
				return "editUser";				
			}
		}
		try {
			userService.updateUser(userData);
			
		} catch (Exception e) {
			bindingResult.rejectValue("email", "such.email.exists");
			return "editUser";	
		}
		
		return "redirect:/profile";
	}

	

}

