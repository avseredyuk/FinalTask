package com.savit.mycassa.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.savit.mycassa.util.exception.CashierHasNotPermissionException;
import com.savit.mycassa.util.exception.OpenedShiftNotExistsException;
import com.savit.mycassa.util.exception.ProductNotFoundException;
import com.savit.mycassa.util.exception.SessionNotStartedYetException;
import com.savit.mycassa.util.exception.ShiftCloseException;

@ControllerAdvice
public class ExceptionController {
	
	
//	@ExceptionHandler({Exception.class})
//	public String repeatSalingTheSameProductHandler(HttpServletRequest req, Exception e, RedirectAttributes redirectAttributes){
//		redirectAttributes.addFlashAttribute("globalError", true);
//		return "redirect:/welcome";
//	}


	@ExceptionHandler({SessionNotStartedYetException.class, OpenedShiftNotExistsException.class})
	public String handleAndRedirectProfile(HttpServletRequest req, Exception e, RedirectAttributes redirectAttributes){
		
		String errorKey = (e instanceof SessionNotStartedYetException) ? "NoSuchOpenedSessionsError"
				: "openedShiftNotExists";
		redirectAttributes.addFlashAttribute(errorKey, true);
		return "redirect:/profile";
	}
	

	@ExceptionHandler({CashierHasNotPermissionException.class})
	public String handleAndRedirectNewSale(HttpServletRequest req, CashierHasNotPermissionException e, RedirectAttributes redirectAttributes){
		redirectAttributes.addFlashAttribute("productAleadyInCheckError", true);
		return "redirect:/sales/" +  e.getEan() + "/new";
	}
	
	
	@ExceptionHandler({ProductNotFoundException.class})
	public String handleAndRedirectProducts(HttpServletRequest req, ProductNotFoundException e, RedirectAttributes redirectAttributes){
		redirectAttributes.addFlashAttribute("productNotFoundError", true);
		return "redirect:/products";

	}
	
	@ExceptionHandler({ShiftCloseException.class})
	public String handleAndRedirectCurrentShift(HttpServletRequest req, ShiftCloseException e, RedirectAttributes redirectAttributes){
		redirectAttributes.addFlashAttribute("shiftCloseError", true);
		return "redirect:/shifts/current";

	}
	
}