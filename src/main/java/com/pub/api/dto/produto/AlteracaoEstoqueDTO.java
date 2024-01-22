package com.pub.api.dto.produto;

import com.pub.api.dto.perdaavaria.PerdaAvariaDTO;
import com.pub.api.dto.unidadeConversao.UnidadeConversaoIdDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlteracaoEstoqueDTO {
	
	private ProdutoIdDTO produto;

	private Integer quantidade;
	
	private String tipoTransacao;
	
	private UnidadeConversaoIdDTO unidadeConversao;
	
	private PerdaAvariaDTO perdaAvaria;
}
