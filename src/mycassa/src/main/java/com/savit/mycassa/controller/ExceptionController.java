package com.savit.mycassa.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.savit.mycassa.util.exception.CashierHasNotPermissionException;
import com.savit.mycassa.util.exception.OpenedShiftNotExistsException;
import com.savit.mycassa.util.exception.ProductNotFoundException;
import com.savit.mycassa.util.exception.ProductNotSavedException;
import com.savit.mycassa.util.exception.SaleNotExistsException;
import com.savit.mycassa.util.exception.SessionNotStartedYetException;
import com.savit.mycassa.util.exception.ShiftCloseException;

@ControllerAdvice
public class ExceptionController {


	@ExceptionHandler({SessionNotStartedYetException.class, 
						OpenedShiftNotExistsException.class})
	public String handleAndRedirectProfile(HttpServletRequest req, Exception e, RedirectAttributes redirectAttributes){
		
		String errorKey = (e instanceof SessionNotStartedYetException) ? "NoSuchOpenedSessionsError"
				: "openedShiftNotExists" ;

		redirectAttributes.addFlashAttribute(errorKey, true);
		return "redirect:/profile";
	}
	

	@ExceptionHandler({CashierHasNotPermissionException.class})
	public String handleAndRedirectNewSale(HttpServletRequest req, CashierHasNotPermissionException e, RedirectAttributes redirectAttributes){
		redirectAttributes.addFlashAttribute("productAleadyInCheckError", true);
		return "redirect:/sales/" +  e.getEan() + "/new";
	}
	
	
	@ExceptionHandler({ProductNotFoundException.class,ProductNotSavedException.class})
	public String handleAndRedirectProducts(HttpServletRequest req, Exception e, RedirectAttributes redirectAttributes){
		
		String errorKey = (e instanceof ProductNotFoundException) ? "productNotFoundError" : "productNotSavedError";
		redirectAttributes.addFlashAttribute(errorKey, true);
		return "redirect:/products";

	}
	
	@ExceptionHandler({ShiftCloseException.class})
	public String handleAndRedirectCurrentShift(HttpServletRequest req, ShiftCloseException e, RedirectAttributes redirectAttributes){
		redirectAttributes.addFlashAttribute("shiftCloseError", true);
		return "redirect:/shifts/current";

	}
	
	@ExceptionHandler({SaleNotExistsException.class})
	public String handleAndRedirectNewSale(HttpServletRequest req, SaleNotExistsException e, RedirectAttributes redirectAttributes){
		redirectAttributes.addFlashAttribute("saleNotExistsError", true);
		return "redirect:/session/requests";
	}
	
}