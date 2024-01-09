package com.pub.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum TipoProblema {

	RECURSO_JA_CADASTRADO("/recurso-ja-cadastrado", "Recurso já cadastrado"),
	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível");
	
	private String uri;
	private String titulo;
	
	private TipoProblema(String uri, String titulo) {
		this.uri = uri;
		this.titulo = titulo;
	}
}
