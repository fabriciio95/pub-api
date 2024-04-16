package com.pub.domain.service.dto;

import java.math.BigDecimal;

import com.pub.domain.model.enums.StatusPromocao;
import com.pub.domain.model.enums.TipoRegraPromocao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegraPromocaoFiltroDTO {

	private Long regraId;
	
	private StatusPromocao status;
	
	private BigDecimal meta;
	
	private BigDecimal valorRegra;
	
	private TipoRegraPromocao tipoRegra;
	
	private Long produtoGratisId;
}
