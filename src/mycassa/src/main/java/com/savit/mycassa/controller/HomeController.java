package com.savit.mycassa.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.savit.mycassa.dto.UserData;
import com.savit.mycassa.service.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomeController {
	
	@Autowired
	private final UserService userService;
	private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);
	

	@GetMapping("/")
	public String redirectToWelcome(Model model) {
		return "redirect:/welcome";
	}
	
	@GetMapping("/welcome")
	public String welcome(Model model) {
		return "welcome";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";	
	}

	
}
