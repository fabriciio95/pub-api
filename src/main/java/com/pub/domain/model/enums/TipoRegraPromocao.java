package com.pub.domain.model.enums;

import lombok.Getter;

@Getter
public enum TipoRegraPromocao {

	DESCONTO_PERCENTUAL("Desconto Percentual"),
	QUANTIDADE_GRATIS("Quantidade Grátis"),
	DESCONTO_FIXO("Desconto Fixo"),
	PRODUTO_GRATIS("Produto Grátis");
	
	private String descricao;
	
	private TipoRegraPromocao(String descricao) {
		this.descricao = descricao;
	}
}
