package com.savit.mycassa.util.exception;

public class CashierHasNotPermissionException extends RuntimeException {

	private String ean;
	
	public CashierHasNotPermissionException(String errorMessage, String ean) {
	    super(errorMessage);
	    this.ean = ean;
	}
	
	
	public String getEan() {
		return ean;
	}
}