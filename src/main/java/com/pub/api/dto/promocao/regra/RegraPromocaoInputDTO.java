package com.pub.api.dto.promocao.regra;

import java.math.BigDecimal;

import com.pub.domain.model.enums.TipoRegraPromocao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegraPromocaoInputDTO {

	@NotNull
	@DecimalMin("0.01")
	private BigDecimal meta;
	
	@NotNull
	@DecimalMin("0.01")
	private BigDecimal valorRegra;
	
	@NotNull
	private TipoRegraPromocao tipoRegra;
	
	private Long produtoGratisId;
	
}
