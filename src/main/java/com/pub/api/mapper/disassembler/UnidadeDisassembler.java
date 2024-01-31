package com.pub.api.mapper.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.unidade.UnidadeInputDTO;
import com.pub.domain.model.Unidade;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UnidadeDisassembler {

	private final ModelMapper modelMapper;
	
	public Unidade toEntidade(UnidadeInputDTO unidadeInputDTO) {
		return modelMapper.map(unidadeInputDTO, Unidade.class);
	}
}
