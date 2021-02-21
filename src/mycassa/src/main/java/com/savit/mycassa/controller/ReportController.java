package com.savit.mycassa.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.savit.mycassa.dto.SessionsDTO;
import com.savit.mycassa.dto.TimeBordersDTO;
import com.savit.mycassa.service.SaleService;
import com.savit.mycassa.service.SessionService;
import com.savit.mycassa.util.exception.CantPrintCheckException;
import com.savit.mycassa.util.exception.SessionNotStartedYetException;
import com.savit.mycassa.util.exception.UserNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@AllArgsConstructor
@PreAuthorize("hasAuthority('SENIOR_CASHIER')")
public class ReportController {

	@Autowired
	private final SessionService sessionService;
	
	@Autowired
	private final SaleService saleService;

	@GetMapping("/reports/sessions")
	public String getSessionsReport(Model model) {
		
		model.addAttribute("borders", new TimeBordersDTO(LocalDateTime.now(), LocalDateTime.now()));
	
		return "reports/reportSessionsForm";
		
	}
	@PostMapping("/reports/sessions")
	public String getSessionsReport(@Valid @ModelAttribute("borders") TimeBordersDTO bordersDTO, 
			RedirectAttributes redirectAttributes, BindingResult bindingResult) {
		
		
		if(bindingResult.hasErrors()) { //javax.validation?
			return "reports/reportSessionsForm";
		}
		
		if(bordersDTO.getTimeFrom().isAfter(bordersDTO.getTimeTo())) {
			bindingResult.rejectValue("timeFrom", "must.be.before.to");	
			bindingResult.rejectValue("timeTo", "must.be.after.from");
			return "reports/reportSessionsForm";
		}
		redirectAttributes.addFlashAttribute("sessions", sessionService.getSessionsForStatistics(bordersDTO));

		return "redirect:/reports/sessions/print";
		
	}

	

	@GetMapping(value = "/reports/sessions/print", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> printSessionsReport(
			@ModelAttribute("sessions") SessionsDTO sessionsDTO, 
			Model model) throws UserNotFoundException, Exception{
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=reportSessions.pdf");

		return ResponseEntity
				.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(sessionService.getSessionReportDocAuth(userDetails, sessionsDTO)));
	}

	
	@GetMapping(value = "/reports/productSales/{ean}/print", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource>  getProductSalesReport (@PathVariable("ean") String ean, Model model) throws SessionNotStartedYetException,
																					CantPrintCheckException{
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=productSales(" + ean + ").pdf");
			return ResponseEntity
			        .ok()
			        .headers(headers)
			        .contentType(MediaType.APPLICATION_PDF)
			        .body(new InputStreamResource(saleService.getProductSalesReportDocAuth(userDetails, ean)));
	}
	
	
}
