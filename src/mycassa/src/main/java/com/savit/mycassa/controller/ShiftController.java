package com.savit.mycassa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.savit.mycassa.service.ShiftService;
import com.savit.mycassa.util.exception.OpenedShiftAlreadyExists;
import com.savit.mycassa.util.exception.OpenedShiftNotExistsException;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/shifts")
@PreAuthorize("hasAuthority('SENIOR_CASHIER')")
public class ShiftController {

	@Autowired
	private final ShiftService shiftService;
	private static final Logger log = LoggerFactory.getLogger(ShiftController.class);


	@GetMapping("current")
	public String getOpenedShift(Model model) {
		try {
			model.addAttribute("shift", shiftService.getOpenedShift());
			return "shift/openedShift";
		} catch (OpenedShiftNotExistsException ex) {
			return "redirect:/shifts/open";
		}
	}
	
	
	@GetMapping("open")
	public String tryToOpenNewShift(Model model) {
		try {
			model.addAttribute("shift", shiftService.createAndGetNewShift());
			return "shift/openedShift";
		} catch (OpenedShiftAlreadyExists ex) {
			return "redirect:/shifts/current";
		}
	}
	
	@GetMapping("close/{shift_id}")
	public String tryToOpenNewShift(@PathVariable Long shift_id, Model model) {
		model.addAttribute("shift", shiftService.closeAndGetShift(shift_id));
		return "shift/closedShift";

	}


}
