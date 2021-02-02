package com.savit.mycassa.util;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Sort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Validator {
	
	public static void validatePageVariables(String pageS, String sizeS) {
		try {
			int pageI = Integer.parseInt(pageS);
			int sizeI = Integer.parseInt(sizeS);
		}catch(NumberFormatException ex) {
			log.error("[VALIDATION ERROR] page: {} and/or size: {} can't formatted from string ot int", pageS, sizeS);
			throw new NumberFormatException("{invalid.params.page.size}");
		}
	}

	public static void validateFilterField(String filterField) {
		if(!Arrays.stream(Constants.FILTRE_FIELDS).anyMatch(filterField::equals)) {
			log.error("[VALIDATION ERROR] invalid field: {}", filterField);
			throw new NoSuchElementException("{invalid.param.attribute}");
		}
	}
	
	
	public static void validateDirection(Sort sort) {
		if(sort == null) {
			log.error("[VALIDATION ERROR] invalid direction: {}");
			throw new NoSuchElementException("{invalid.param.direction}");
		}
	}
}
