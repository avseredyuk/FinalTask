package com.savit.mycassa.controller;

import java.security.Timestamp;
import java.time.LocalDateTime;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.savit.mycassa.dto.UserData;
import com.savit.mycassa.service.UserService;

@Controller
public class RegistrationController {

	private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);
//	private UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
//	userService.signUpUser(userDTO);
	@GetMapping("/registration")
	public String showForm(@ModelAttribute UserData userData,  Model model) {
//		UserData userData = new UserData("david", "savit", "dav@gmail.com", "David");
		model.addAttribute("userData", userData);
		return "registration";
	}

	@PostMapping("/registration")
	public String registrateNewUser(@Valid UserData userData, 
			BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return "registration";
		}
		
		log.info(" >> userDTO: {}", userData.toString());

		return "redirect:/login";
	}

}
