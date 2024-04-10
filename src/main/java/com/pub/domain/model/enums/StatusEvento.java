package com.pub.domain.model.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum StatusEvento {

	ATIVO,
	INICIADO,
	FINALIZADO,
	CANCELADO;
	
	public List<StatusEvento> getStatusPossiveisParaMudanca() {
		List<StatusEvento> statusPossiveisParaMudanca = new ArrayList<>();
		
		if(this.equals(ATIVO)) {
			statusPossiveisParaMudanca.addAll(Arrays.asList(ATIVO, INICIADO, CANCELADO));
		} else if(this.equals(INICIADO)) {
			statusPossiveisParaMudanca.addAll(Arrays.asList(INICIADO, ATIVO, FINALIZADO, CANCELADO));
		} else if(this.equals(FINALIZADO)) {
			statusPossiveisParaMudanca.addAll(Arrays.asList(FINALIZADO, INICIADO, CANCELADO));
		} else {
			statusPossiveisParaMudanca.addAll(Arrays.asList(CANCELADO, INICIADO, FINALIZADO, ATIVO));
		} 
		
		return statusPossiveisParaMudanca;
	}
}
