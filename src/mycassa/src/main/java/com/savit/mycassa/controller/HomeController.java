package com.savit.mycassa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class HomeController {
	
	private static final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	
	@GetMapping("/")
	public String root(Model model) {
		return "redirect:/welcome";
	}
	
	@GetMapping("/welcome")
	public String welcome(Model model) {
		return "welcome";
	}
	
	@GetMapping("/login")
	public String login() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth==null || auth instanceof AnonymousAuthenticationToken) {
			return "login";				
		}						
		return "redirect:/welcome";
	}

	
}
