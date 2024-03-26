package com.pub.domain.service.dto;

import java.time.LocalDate;


import com.pub.domain.model.enums.ModalidadeLancamento;
import com.pub.domain.model.enums.TipoLancamento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LancamentoDTO {
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;
	
	private Long lancamentoId;
	
	private TipoLancamento tipoLancamento;
	
	private ModalidadeLancamento modalidade;
	
	private Long produtoId;
	
}