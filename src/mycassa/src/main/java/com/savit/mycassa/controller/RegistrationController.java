package com.savit.mycassa.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.savit.mycassa.dto.UserData;
import com.savit.mycassa.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RequestMapping("/registration")
@Controller
public class RegistrationController extends ResponseEntityExceptionHandler {
	

	private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);
	
	@Autowired
	private final UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	@GetMapping
	public String showForm(@ModelAttribute UserData userData,  Model model) {
		model.addAttribute("userData", userData);
		log.info(" >> model: {}", model.toString());
		return "registration";
	}

	@PostMapping
	public String registrateNewUser(@Valid UserData userData, 
			BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			log.info(" >> userDTO: {}", userData.toString());
			return "registration";
		}
		log.info(" >> userDTO: {}", userData.toString());
		try {
			userService.signUpUser(userData);			
		}catch(Exception ex) {
			bindingResult.rejectValue("email", "error.user", "An account already exists for this email.");
		}

		return "redirect:/login";
	}

}
