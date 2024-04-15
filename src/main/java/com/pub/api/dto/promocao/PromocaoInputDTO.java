package com.pub.api.dto.promocao;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromocaoInputDTO {

	@DateTimeFormat(iso = ISO.DATE_TIME)
	@NotNull
	private OffsetDateTime dataInicio;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@NotNull
	private OffsetDateTime dataFim;
	
	private String descricao;
	
	@NotNull
	private Boolean aplicarTodasRegras;
	
}
