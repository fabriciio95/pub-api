package com.pub.domain.service.dto;

import java.time.LocalDate;


import com.pub.domain.model.enums.ModalidadeLancamento;
import com.pub.domain.model.enums.TipoLancamento;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LancamentoFiltroDTO {
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;
	
	private Long lancamentoId;
	
	private TipoLancamento tipoLancamento;
	
	private ModalidadeLancamento modalidade;
	
	private Long produtoId;
	
	private String descricao;
	
}