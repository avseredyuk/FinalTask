package com.savit.mycassa.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.savit.mycassa.dto.UserDTO;
import com.savit.mycassa.service.UserService;
import com.savit.mycassa.util.exception.UserNotFoundException;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
//@PreAuthorize("hasAnyAuthority('COMMODITY_EXPERT','SENIOR_CASHIER', 'CASHIER')")
@RequestMapping("/profile")
public class UserController {

	@Autowired
	private final UserService userService;

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping
	public String getUserProfile(Model model) throws UserNotFoundException {
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		model.addAttribute("user", userService.getUserByEmailAuth(userDetails));
		return "profile";
	}

	@GetMapping("edit")
	public String getUserEditForm(Model model) throws UserNotFoundException {
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 
		model.addAttribute("user", userService.getUserByEmailAuth(userDetails)); 

		return "editUser";			
		
	}

	@PostMapping("/edit")
	public String editUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			if(bindingResult.getAllErrors().size() > 1) {
				return "editUser";				
			}
		}
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			userService.updateUser(userDTO, userDetails);			
		}catch(Exception ex) {
			bindingResult.rejectValue("email", "such.email.exists");			
			return "editUser";
		}
		
		return "redirect:/profile";
	}

	

}

