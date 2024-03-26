package com.pub.domain.model.enums;

import lombok.Getter;

@Getter
public enum ModalidadeLancamento {

	ALTERACAO_ESTOQUE("Alteração de Estoque"),
	EVENTO("Evento"),
	ATENDIMENTO("Atendimento");
	
	private String descricao;
	
	private ModalidadeLancamento(String descricao) {
		this.descricao = descricao;
	}
}
