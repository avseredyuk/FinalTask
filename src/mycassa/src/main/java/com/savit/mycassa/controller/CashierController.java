package com.savit.mycassa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.savit.mycassa.dto.SessionData;
import com.savit.mycassa.dto.UserData;
import com.savit.mycassa.service.SessionService;
import com.savit.mycassa.service.UserService;
import com.savit.mycassa.util.exception.SessionNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
@AllArgsConstructor
public class CashierController {
	
	@Autowired
	private final SessionService sessionService;
	
	@Autowired
	private final UserService userService;
	
	
	@PostMapping("/session/new")
	public String getNewSessionFrame(Model model) {
		SessionData sessionData = sessionService.addSession(userService.getPrincipal().get());
		return "redirect:/products";
	}
	
//	@GetMapping("/session/{sessionId}")
//	public String createSessionForUser(@PathVariable("sessionId") Long sessionId, 
//			Model model) throws SessionNotFoundException {
//		SessionData sessionData = sessionService.getSessionById(sessionId);
//		log.info("sessionData by id: {}", sessionData);
//		model.addAttribute("sessionData", sessionData);
//		return "selling";
//	}

}
