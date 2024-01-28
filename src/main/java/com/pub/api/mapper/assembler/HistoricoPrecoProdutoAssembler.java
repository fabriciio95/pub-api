package com.pub.api.mapper.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.historico.HistoricoPrecoProdutoDTO;
import com.pub.domain.model.HistoricoPrecoProduto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class HistoricoPrecoProdutoAssembler {

	private final ModelMapper modelMapper;
	
	public List<HistoricoPrecoProdutoDTO> toListDTO(List<HistoricoPrecoProduto> entityList) {
		return entityList.stream().map(hp -> modelMapper.map(hp, HistoricoPrecoProdutoDTO.class)).toList();
	}
}
