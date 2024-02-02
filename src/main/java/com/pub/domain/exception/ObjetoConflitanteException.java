package com.pub.domain.exception;

public class ObjetoConflitanteException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ObjetoConflitanteException(String message) {
		super(message);
	}
	
	public ObjetoConflitanteException(String message, Throwable cause) {
		super(message, cause);
	}
}
