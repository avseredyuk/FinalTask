package com.savit.mycassa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.savit.mycassa.service.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MainController {
	
	
	@Autowired
	private final UserService userService;
	
	
	@GetMapping("/welcome")
	public String welcome() {
		return "welcome";
	}
	
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/registration")
	public String registration() {
		return "registration";
	}
	

	
	
}
