package com.pub.api.mapper.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.produto.AlteracaoEstoqueDTO;
import com.pub.domain.service.dto.TransacaoEstoqueDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TransacaoEstoqueDisassembler {

	private final ModelMapper modelMapper;
	
	public TransacaoEstoqueDTO toTransacaoEstoqueDTO(AlteracaoEstoqueDTO alteracaoEstoqueDTO) {
		return modelMapper.map(alteracaoEstoqueDTO, TransacaoEstoqueDTO.class);
	}
}
