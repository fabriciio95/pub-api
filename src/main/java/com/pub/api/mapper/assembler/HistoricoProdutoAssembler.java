package com.pub.api.mapper.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.historico.HistoricoProdutoDTO;
import com.pub.domain.model.HistoricoProduto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class HistoricoProdutoAssembler {

	private final ModelMapper modelMapper;
	
	public List<HistoricoProdutoDTO> toListDTO(List<HistoricoProduto> entityList) {
		return entityList.stream().map(hp -> modelMapper.map(hp, HistoricoProdutoDTO.class)).toList();
	}
}
