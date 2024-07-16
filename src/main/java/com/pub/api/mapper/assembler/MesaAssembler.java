package com.pub.api.mapper.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.mesa.MesaDTO;
import com.pub.domain.model.Mesa;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MesaAssembler {

	private final ModelMapper mapper;
	
	public MesaDTO toDTO(Mesa mesa) {
		return mapper.map(mesa, MesaDTO.class);
	}
	
	public List<MesaDTO> toListDTO(List<Mesa> mesas) {
		return mesas.stream().map(p -> mapper.map(p, MesaDTO.class)).toList();
	}
}
