package com.pub.api.dto.cliente;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ClienteDTO {
	
	private Long id;
	
	private String nome;
	
	private String telefone;
	
	private String cpf;

}
