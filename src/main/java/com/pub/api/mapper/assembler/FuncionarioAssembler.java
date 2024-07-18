package com.pub.api.mapper.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.funcionario.FuncionarioDTO;
import com.pub.domain.model.Funcionario;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FuncionarioAssembler {

	private final ModelMapper mapper;
	
	public FuncionarioDTO toDTO(Funcionario funcionario) {
		return mapper.map(funcionario, FuncionarioDTO.class);
	}
	
	public List<FuncionarioDTO> toListDTO(List<Funcionario> funcionarios) {
		return funcionarios.stream().map(this::toDTO).toList();
	}
}
