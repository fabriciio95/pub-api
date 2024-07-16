package com.pub.api.mapper.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.mesa.MesaInputDTO;
import com.pub.domain.model.Mesa;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MesaDisassembler {

	private final ModelMapper modelMapper;
	
	public Mesa toEntidade(MesaInputDTO mesaInputDTO) {
		return modelMapper.map(mesaInputDTO, Mesa.class);
	}
}
