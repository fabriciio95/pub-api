package com.pub.domain.service.dto;

import java.time.LocalDate;

import com.pub.domain.model.enums.StatusEvento;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EventoFiltroDTO {

	private LocalDate dataInicioPeriodoInicio;
	
	private LocalDate dataFimPeriodoInicio;
	
	private LocalDate dataInicioPeriodoFim;
	
	private LocalDate dataFimPeriodoFim;
	
	private Long id;
	
	private String descricao;
	
	private StatusEvento status;
}
