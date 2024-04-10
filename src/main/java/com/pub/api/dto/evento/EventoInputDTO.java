package com.pub.api.dto.evento;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.pub.api.validation.ValidationGroups.AtualizacaoGroup;
import com.pub.domain.model.enums.StatusEvento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventoInputDTO {

	@NotBlank
	private String descricao;
	
	@NotNull(groups = AtualizacaoGroup.class)
	private StatusEvento status;
	
	@NotNull
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime dataHoraInicioEvento;
	
	@NotNull
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime dataHoraFimEvento;
}
