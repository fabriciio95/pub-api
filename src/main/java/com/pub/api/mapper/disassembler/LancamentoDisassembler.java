package com.pub.api.mapper.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.lancamento.LancamentoInputDTO;
import com.pub.domain.model.Lancamento;
import com.pub.domain.model.enums.ModalidadeLancamento;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LancamentoDisassembler {

	private final ModelMapper modelMapper;
	
	public Lancamento toEntidade(LancamentoInputDTO lancamentoInputDTO) {
		Lancamento lancamento = modelMapper.map(lancamentoInputDTO, Lancamento.class);
		
		if(lancamentoInputDTO.getModalidade() != null) {
			lancamento.setModalidade(ModalidadeLancamento.findModalidadePorDescricao(lancamentoInputDTO.getModalidade()));
		}
		
		return lancamento;
	}
}
