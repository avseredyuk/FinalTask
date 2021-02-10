package com.savit.mycassa.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.savit.mycassa.util.exception.CashierHasNotPermissionException;
import com.savit.mycassa.util.exception.EmptySalesListException;
import com.savit.mycassa.util.exception.SessionNotStartedYetException;

@ControllerAdvice
public class ExceptionController {

//	@Autowired
//	private MessageSource messageSource;
//	
//	@Autowired
//	private LocalValidatorFactoryBean localValidatorFactoryBean;


	@ExceptionHandler(value = SessionNotStartedYetException.class)
	public ModelAndView sessionNotStartedHandler(HttpServletRequest req, Exception e){

		ModelAndView modelAndView = new ModelAndView();

		
		modelAndView.setViewName("redirect:/profile");

		return modelAndView;

	}

	
	@ExceptionHandler({EmptySalesListException.class})
	public String buildCheckHandler(HttpServletRequest req, EmptySalesListException e, RedirectAttributes redirectAttributes){
		redirectAttributes.addFlashAttribute("error", e.getMessage());

		return "redirect:/session/" + e.getSessionId() + "/check/overview";

	}
	
	
	

	
	@ExceptionHandler({CashierHasNotPermissionException.class})
	public String repeatSalingTheSameProductHandler(HttpServletRequest req, CashierHasNotPermissionException e, RedirectAttributes redirectAttributes){
//		messageSource.getMessage(e.getLocalizedMessage(), Locale.)
//		redirectAttributes.addFlashAttribute("error", e.getLocalizedMessage());
		
		redirectAttributes.addFlashAttribute("theSameProductsError", true);
		return "redirect:/sales/" +  e.getEan() + "/new";

	}
	
}