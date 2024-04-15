package com.pub.api.mapper.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.promocao.PromocaoInputDTO;
import com.pub.domain.model.Promocao;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PromocaoDisassembler {

	private final ModelMapper modelMapper;
	
	public Promocao toEntidade(PromocaoInputDTO promocaoInputDTO) {
		return modelMapper.map(promocaoInputDTO, Promocao.class);
	}
}
