package com.pub.api.mapper.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.funcionario.FuncionarioInputDTO;
import com.pub.domain.model.Funcionario;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FuncionarioDisassembler {

	private final ModelMapper modelMapper;
	
	public Funcionario toEntidade(FuncionarioInputDTO funcionarioInputDTO) {
		return modelMapper.map(funcionarioInputDTO, Funcionario.class);
	}
}
