package com.savit.mycassa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.session.StatusSession;
import com.savit.mycassa.service.SessionService;
import com.savit.mycassa.service.UserService;
import com.savit.mycassa.util.exception.NoOpenedSessionException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@AllArgsConstructor
public class SessionController {
//TODO logging
	//FIXME remove trigger
	@Autowired
	private final SessionService sessionService;

	@Autowired
	private final UserService userService;

	
	 @GetMapping("/session/new") public String startSession(Model model) {
		 try {
			 model.addAttribute("sessionData", sessionService.createNewSession());
			 return "redirect:/products";
		 }catch(Exception ex) {
			 return "redirect:/session";
		 }
	 }
	 

		@GetMapping("/session")
		public String createSessionForUser(Model model) throws NoOpenedSessionException{
			model.addAttribute("sessionData", sessionService.getAuthUserOpenedSession());
			return "notEndedSession";
		} 
		
		@GetMapping("/session/editrequest")
		public String editRequestSessionFromCashier(Model model) throws NoOpenedSessionException{
			sessionService.updateStatusSession(StatusSession.WAITING);
			return "redirect:/sales/check/overview";
		} 
		//TODO group .html files by their topics
		//TODO refactor conrollers method names
		@GetMapping("/session/requests")
		public String getRequestsFromCashiers(Model model){
			List<Session> sessions = sessionService.getSessionsByStatus(StatusSession.WAITING).getSessions();
			
			for(Session s : sessions) {
				System.out.println(s);
			}
			
			model.addAttribute("sessions", sessionService.getSessionsByStatus(StatusSession.WAITING));			
			return "waitingSessions";
		} 
	
}
