package com.pub.domain.model.enums;

import com.pub.domain.exception.ViolacaoRegraNegocioException;

import lombok.Getter;

@Getter
public enum ModalidadeLancamento {

	ALTERACAO_ESTOQUE("Alteração de Estoque"),
	EVENTO("Evento"),
	ATENDIMENTO("Atendimento"),
	OUTROS("Outros");
	
	private String descricao;
	
	private ModalidadeLancamento(String descricao) {
		this.descricao = descricao;
	}
	
	public static ModalidadeLancamento findModalidadePorDescricao(String descricao) {
		
		for(ModalidadeLancamento modalidade : values()) {
			if(modalidade.getDescricao().equalsIgnoreCase(descricao)) {
				return modalidade;
			}
		}
		
		String mensagemErro = descricao == null ? "Modalidade informada inválida" : String.format("Modalidade ('%s') informada inválida", descricao);
		
		throw new ViolacaoRegraNegocioException(mensagemErro);
	}
}
