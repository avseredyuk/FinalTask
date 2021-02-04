package com.savit.mycassa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.savit.mycassa.dto.SessionData;
import com.savit.mycassa.dto.SessionsData;
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

	
	 @GetMapping("/sessions/new") public String startSession(Model model) {
		 try {
			 model.addAttribute("sessionData", sessionService.createNewSession());
			 return "redirect:/products";
		 }catch(Exception ex) {
			 return "redirect:/sessions";
		 }
	 }
	 

	@GetMapping("/sessions")
	public String createSessionForUser(Model model){
		log.info("SESSIONS rollback");
		SessionsData sessionsData = sessionService.getAllAuthUserOpenedSessions();
		model.addAttribute("sessionsData", sessionsData);
		return "sessions";
	} 

}
