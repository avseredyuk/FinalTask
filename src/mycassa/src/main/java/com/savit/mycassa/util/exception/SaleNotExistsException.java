package com.savit.mycassa.util.exception;

public class SaleNotExistsException extends RuntimeException {
	
	private Long sessionId;
	
	public SaleNotExistsException(Long sessionId) {
		super();
		this.sessionId = sessionId;
	}

	public Long getSessionId() {
		return sessionId;
	}
}
