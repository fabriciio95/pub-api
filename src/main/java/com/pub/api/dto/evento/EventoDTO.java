package com.pub.api.dto.evento;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.pub.domain.model.enums.StatusEvento;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class EventoDTO {

	private Long id;
	
	private String descricao;
	
	private StatusEvento status;
	
	private OffsetDateTime dataHoraInicioEvento;
	
	private OffsetDateTime dataHoraFimEvento;
	
	private OffsetDateTime dataHoraCancelamento;
	
	private OffsetDateTime dataHoraFinalizacao;
	
	private OffsetDateTime dataCadastro;
}
