package com.savit.mycassa.util.exception;

public class NoOpenedSessionException extends RuntimeException {

	public NoOpenedSessionException(String errorMessage) {
	    super(errorMessage);
	}
	
	
}