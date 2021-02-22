package com.savit.mycassa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.savit.mycassa.dto.SessionDTO;
import com.savit.mycassa.entity.session.StatusSession;
import com.savit.mycassa.service.SessionService;
import com.savit.mycassa.util.exception.CantPrintCheckException;
import com.savit.mycassa.util.exception.NotClosedSessionExistsException;
import com.savit.mycassa.util.exception.SessionNotStartedYetException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/session")
public class SessionController {

	@Autowired
	private final SessionService sessionService;
	

	@GetMapping
	public String createSessionForUser(Model model) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		model.addAttribute("sessionDTO", sessionService.getNotEndedSessionAuth(userDetails));
		return "session/notEndedSession";
	}
	
	
	
	@GetMapping("new")
	public String startSession(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			sessionService.createNewSessionAuth(userDetails);
			return "redirect:/products";
		} catch (NotClosedSessionExistsException ex) {
			return "redirect:/session";
		}
	}
	

	@GetMapping("{sessionId}/editrequest")
	public String callCashierToEdit(@PathVariable("sessionId") Long sessionId, Model model) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		sessionService.updateStatusSessionAuth(userDetails, StatusSession.WAITING);
		return String.format("redirect:/session/%d/check/overview",sessionId);
	}


	@GetMapping("requests")
	public String getwaitingSessionsFromCashiers(Model model) {
		model.addAttribute("sessions", sessionService.getSessionsByStatus(StatusSession.WAITING));
		return "session/waitingSessions";
	}

	@GetMapping("check")
	public String getRedirectToCheckOverviewWithSessionId(Model model) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		SessionDTO sessionDTO = sessionService.getNotEndedSessionAuth(userDetails);
		return String.format("redirect:/session/%d/check/overview",sessionDTO.getId() );
	}


	@GetMapping("{sessionId}/check/overview")
	public String getSalesList(@PathVariable("sessionId") Long sessionId, Model model)
			throws SessionNotStartedYetException {

		model.addAttribute("check", sessionService.getCheckDTO(sessionId));

		return "session/checkOverview";
	}

	@GetMapping(value = "{sessionId}/check/print", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> getSalesReport(@PathVariable("sessionId") Long sessionId, Model model) 
			throws NumberFormatException, CantPrintCheckException{
		
		var headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=check.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(sessionService.getCheckAndCloseSession(sessionId)));
	}
	
	
	@GetMapping("{sessionId}/close")
	public String deleteSession(@PathVariable("sessionId") Long sessionId, Model model) {
		log.info("START CLOSING");

		sessionService.cancelSession(sessionId);

		return "redirect:/session/requests";
	}



	// TODO logging

}
