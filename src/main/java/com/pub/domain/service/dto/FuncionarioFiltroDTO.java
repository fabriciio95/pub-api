package com.pub.domain.service.dto;

import com.pub.domain.model.enums.FuncaoFuncionario;
import com.pub.domain.model.enums.StatusFuncionario;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FuncionarioFiltroDTO {

	private Long funcionarioId;
	
	private String nome;
	
	private String telefone;
	
	private String cpf;
	
	private StatusFuncionario status;
	
	private FuncaoFuncionario funcao;
}
