package com.pub.api.dto.funcionario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.pub.domain.model.enums.FuncaoFuncionario;
import com.pub.domain.model.enums.StatusFuncionario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class FuncionarioDTO {
	
	private Long id;
	
	private FuncaoFuncionario funcao;
	
	private StatusFuncionario status;
	
	private String telefone;
	
	private String cpf;
	
	private String nome;

}
