package com.pub.api.mapper.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.lancamento.LancamentoDTO;
import com.pub.domain.model.Lancamento;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LancamentoAssembler {

	private final ModelMapper modelMapper;
	
	public LancamentoDTO toDto(Lancamento lancamento) {
		LancamentoDTO lancamentoDto = modelMapper.map(lancamento, LancamentoDTO.class);
		
		if(lancamento.getModalidade() != null) {
			lancamentoDto.setModalidade(lancamento.getModalidade().getDescricao());
		}
		
		return lancamentoDto;
	}
	
	public List<LancamentoDTO> toListDto(List<Lancamento> lancamentos) {
		return lancamentos.stream().map(this::toDto).toList();
	}
}
