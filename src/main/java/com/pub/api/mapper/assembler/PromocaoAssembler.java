package com.pub.api.mapper.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.promocao.PromocaoDTO;
import com.pub.domain.model.Promocao;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PromocaoAssembler {

	private final ModelMapper modelMapper;
	
	public PromocaoDTO toDto(Promocao promocao) {
		return modelMapper.map(promocao, PromocaoDTO.class);
	}
	
	public List<PromocaoDTO> toListDto(List<Promocao> promocoes) {
		return promocoes.stream().map(this::toDto).toList();
	}
}
