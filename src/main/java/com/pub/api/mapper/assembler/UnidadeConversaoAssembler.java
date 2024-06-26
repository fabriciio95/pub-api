package com.pub.api.mapper.assembler;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.unidadeConversao.UnidadeConversaoDTO;
import com.pub.domain.model.UnidadeConversao;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UnidadeConversaoAssembler {

	private final ModelMapper modelMapper;
	
	public UnidadeConversaoDTO toDto(UnidadeConversao unidadeConversao) {
		return modelMapper.map(unidadeConversao, UnidadeConversaoDTO.class);
	}
	
	public List<UnidadeConversaoDTO> toListDto(Collection<UnidadeConversao> unidadesConversao) {
		return unidadesConversao.stream().map(c -> modelMapper.map(c, UnidadeConversaoDTO.class)).toList();
	}
}
