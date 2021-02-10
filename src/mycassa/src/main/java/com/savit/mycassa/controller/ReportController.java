package com.savit.mycassa.controller;

import java.io.BufferedInputStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.savit.mycassa.dto.SessionsDTO;
import com.savit.mycassa.dto.TimeBordersDTO;
import com.savit.mycassa.service.SessionService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@AllArgsConstructor
//@RequestMapping("/reports")
@PreAuthorize("hasAuthority('SENIOR_CASHIER')")
public class ReportController {

	@Autowired
	private final SessionService sessionService;

	@GetMapping("/reports/sessions")
	public String getSessionsReport(Model model) {
		
		model.addAttribute("borders", new TimeBordersDTO());
	
		return "reports/reportSessionsForm";
		
	}
	
	
	@PostMapping("/reports/sessions")//
	public String getSessionsReport(@Valid @ModelAttribute("borders") TimeBordersDTO bordersDTO, 
			RedirectAttributes redirectAttributes, BindingResult bindingResult) {
		
		
		if(bindingResult.hasErrors()) {
			return "reports/reportSessionsForm";
		}
		log.info("borders : {}", bordersDTO);
		
		if(bordersDTO.getTimeFrom().isAfter(bordersDTO.getTimeTo())) {
			bindingResult.rejectValue("timeFrom", "must.be.before.to");	
			bindingResult.rejectValue("timeTo", "must.be.after.from");
			return "reports/reportSessionsForm";
		}
		//FIXME
		redirectAttributes.addFlashAttribute("sessions", sessionService.getSessionsForStatistics(bordersDTO));
//		model.addAttribute("sessions", sessionService.getSessionsForStatistics(bordersDTO));
		
//		log.info("from: {}, to: {}", bordersDTO.getTimeFrom().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), 
//				bordersDTO.getTimeTo().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
//
//	
		return "redirect:/reports/sessions/print";
		
	}

	

	@GetMapping(value = "/reports/sessions/print", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> printSessionsReport(
			@ModelAttribute("sessions") SessionsDTO sessionsDTO, 
			Model model){
		
		
		var headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=reportSessions.pdf");
//headers.add("Refresh", "10; url = /profile");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(new BufferedInputStream(sessionService.getSessionReportDoc(sessionsDTO))));
	}

}
