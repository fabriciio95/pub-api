package com.pub.api.mapper.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.unidadeConversao.UnidadeConversaoInputDTO;
import com.pub.domain.model.UnidadeConversao;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UnidadeConversaoDisassembler {

	private final ModelMapper modelMapper;
	
	public UnidadeConversao toEntidade(UnidadeConversaoInputDTO unidadeConversaoInputDTO) {
		return modelMapper.map(unidadeConversaoInputDTO, UnidadeConversao.class);
	}
}
