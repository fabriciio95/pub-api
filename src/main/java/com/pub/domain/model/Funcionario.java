package com.pub.domain.model;

import com.pub.domain.model.enums.FuncaoFuncionario;
import com.pub.domain.model.enums.StatusFuncionario;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Funcionario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private FuncaoFuncionario funcao;
	
	@Enumerated(EnumType.STRING)
	private StatusFuncionario status;
	
	private String telefone;
	
	private String cpf;
	
	private String nome;
}
