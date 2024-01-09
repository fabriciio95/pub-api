package com.pub.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class Problema {

	private Integer status;
	
	private OffsetDateTime timestamp;
	
	private String tipo;
	
	private String titulo;
	
	private String detalhe;
	
	private String mensagemUsuario;
	
	private List<Objeto> objetos;
	
	@Getter
	@Builder
	public static class Objeto {
		
		private String nome;
		
		private String mensagemUsuario;
	}
}
