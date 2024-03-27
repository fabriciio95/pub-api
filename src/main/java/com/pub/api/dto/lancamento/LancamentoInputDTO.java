package com.pub.api.dto.lancamento;

import java.math.BigDecimal;

import com.pub.domain.model.enums.TipoLancamento;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LancamentoInputDTO {

	@NotNull
	private TipoLancamento tipo;
	
	@NotBlank
	private String modalidade;
	
	@NotNull
	@DecimalMin("0.01")
	private BigDecimal valor;
	
	@NotBlank
	private String descricao;

}
