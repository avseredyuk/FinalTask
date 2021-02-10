package com.savit.mycassa.util.exception;

public class EmptySalesListException extends RuntimeException {
	
	private Long sessionId;
	
	public EmptySalesListException(String message, Long id) {
		super(message);
		this.sessionId =  id;
	}
	
	public String getSessionId() {
		return String.valueOf(sessionId);
	}

}
