package com.pub.domain.model.enums;

import com.pub.domain.exception.ViolacaoRegraNegocioException;

public enum TipoTransacao {

	VENDA,
	COMPRA,
	PERDA;
	
	
	public static TipoTransacao findTipoTransacao(String tipoTransacao) {
		for(TipoTransacao transacao : values()) {
			if(transacao.name().equals(tipoTransacao)) {
				return transacao;
			}
		}
		
		throw new ViolacaoRegraNegocioException(String.format("Tipo de transação ('%s') informada inválida", tipoTransacao));
	}
}
