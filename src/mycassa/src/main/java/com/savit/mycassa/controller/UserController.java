package com.savit.mycassa.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.savit.mycassa.dto.UserDTO;
import com.savit.mycassa.service.UserService;
import com.savit.mycassa.util.exception.UserNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/profile")
public class UserController {

	@Autowired
	private final UserService userService;

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	
	@GetMapping
	public String getUserProfile(Model model) throws UserNotFoundException {
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		model.addAttribute("user", userService.getUserByEmailAuth(userDetails));
		return "user/profile";
	}

	@GetMapping("edit")
	public String getUserEditForm(Model model) throws UserNotFoundException {
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 
		model.addAttribute("user", userService.getUserByEmailAuth(userDetails)); 

		return "user/editUser";			
		
	}

	@PostMapping("/edit")
	public String editUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			if(bindingResult.getAllErrors().size() > 1) {
				return "user/editUser";				
			}
		}
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			userService.updateUser(userDTO, userDetails);			
		}catch(Exception ex) {
			bindingResult.rejectValue("email", "valid.user.email.exists");			
			return "user/editUser";
		}
		
		return "redirect:/logout";
	}

	

}

