package com.pub.domain.exception;

import lombok.Getter;

@Getter
public class ViolacaoRegraNegocioException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String mensagemDesenvolvedor;

	public ViolacaoRegraNegocioException(String message) {
		super(message);
	}
	
	public ViolacaoRegraNegocioException(String mensagemUsuario, String mensagemDesenvolvedor) {
		super(mensagemUsuario);
		this.mensagemDesenvolvedor = mensagemDesenvolvedor;
	}
}
