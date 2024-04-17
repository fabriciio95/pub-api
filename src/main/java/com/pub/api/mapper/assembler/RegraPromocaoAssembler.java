package com.pub.api.mapper.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.promocao.regra.RegraPromocaoDTO;
import com.pub.domain.model.RegraPromocao;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RegraPromocaoAssembler {

	private final ModelMapper modelMapper;
	
	public RegraPromocaoDTO toDto(RegraPromocao regra) {
		return modelMapper.map(regra, RegraPromocaoDTO.class);
	}
	
	public List<RegraPromocaoDTO> toListDto(List<RegraPromocao> regras) {
		return regras.stream().map(this::toDto).toList();
	}
}
