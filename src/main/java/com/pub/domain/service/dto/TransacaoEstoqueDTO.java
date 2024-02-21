package com.pub.domain.service.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransacaoEstoqueDTO {

	private Long produtoId;
	
	private Integer quantidade;
	
	private BigDecimal valorTotal;
	
	private Long unidadeConversaoId;
	
	private String tipoTransacao;
	
	private PerdaAvariaDTO perdaAvariaDTO;
}
