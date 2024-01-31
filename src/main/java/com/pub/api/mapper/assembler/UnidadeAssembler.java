package com.pub.api.mapper.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.unidade.UnidadeDTO;
import com.pub.domain.model.Unidade;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UnidadeAssembler {

	private final ModelMapper modelMapper;
	
	public UnidadeDTO toDto(Unidade unidade) {
		return modelMapper.map(unidade, UnidadeDTO.class);
	}
	
	public List<UnidadeDTO> toListDto(List<Unidade> unidades) {
		return unidades.stream().map(c -> modelMapper.map(c, UnidadeDTO.class)).toList();
	}
}
