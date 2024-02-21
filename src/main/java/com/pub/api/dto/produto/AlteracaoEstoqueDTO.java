package com.pub.api.dto.produto;

import java.math.BigDecimal;

import com.pub.api.dto.perdaavaria.PerdaAvariaInputDTO;
import com.pub.api.dto.unidadeConversao.UnidadeConversaoIdDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlteracaoEstoqueDTO {
	
	private ProdutoIdDTO produto;

	private Integer quantidade;
	
	private BigDecimal valorTotal;
	
	private String tipoTransacao;
	
	private UnidadeConversaoIdDTO unidadeConversao;
	
	private PerdaAvariaInputDTO perdaAvaria;
}
