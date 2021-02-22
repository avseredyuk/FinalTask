package com.savit.mycassa.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.savit.mycassa.dto.UserDTO;
import com.savit.mycassa.service.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomeController {
	
	private static final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	
	@GetMapping("/welcome")
	public String welcome(Model model) {
		return "welcome";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";	
	}

	
}
