package com.pub.domain.service.dto;

import java.time.LocalDate;

import com.pub.domain.model.enums.StatusPromocao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PromocaoFiltroDTO {

	private LocalDate dataInicio;
	
	private LocalDate dataFim;
	
	private Long id;
	
	private String descricao;
	
	private StatusPromocao status;
}
