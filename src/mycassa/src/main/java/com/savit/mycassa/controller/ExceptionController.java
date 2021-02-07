package com.savit.mycassa.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.savit.mycassa.util.exception.CantPrintCheckException;
import com.savit.mycassa.util.exception.EmptySalesListException;
import com.savit.mycassa.util.exception.SessionNotStartedYetException;

@ControllerAdvice
public class ExceptionController {

//	@ExceptionHandler(value = Exception.class)
//	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
//
//		ModelAndView modelAndView = new ModelAndView();
//
//		modelAndView.addObject("message", e.getMessage());
//		modelAndView.setViewName("profile");
//
//		return modelAndView;
//	}

	@ExceptionHandler(value = SessionNotStartedYetException.class)
	public ModelAndView sessionNotStartedHandler(HttpServletRequest req, Exception e){

		ModelAndView modelAndView = new ModelAndView();

		
		modelAndView.setViewName("redirect:/profile");

		return modelAndView;

	}
	
	
	@ExceptionHandler({EmptySalesListException.class,  CantPrintCheckException.class})
	public String buildCheckHandler(HttpServletRequest req, Exception e, RedirectAttributes redirectAttributes){
		redirectAttributes.addFlashAttribute("error", e.getMessage());
//		model.addAttribute("error", e.getMessage());
		return "redirect:/sales/check/overview";

	}
	
}