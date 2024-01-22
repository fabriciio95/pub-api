package com.pub.domain.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransacaoEstoqueDTO {

	private Long produtoId;
	
	private Integer quantidade;
	
	private Long unidadeConversaoId;
	
	private String tipoTransacao;
	
	private PerdaAvariaDTO perdaAvariaDTO;
}
