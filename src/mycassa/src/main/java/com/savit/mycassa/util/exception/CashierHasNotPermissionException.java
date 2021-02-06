package com.savit.mycassa.util.exception;

public class CashierHasNotPermissionException extends RuntimeException {

	public CashierHasNotPermissionException(String errorMessage) {
	    super(errorMessage);
	}
}