package com.pub.domain.exception;

public class ViolacaoRegraNegocioException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ViolacaoRegraNegocioException(String message) {
		super(message);
	}
}
