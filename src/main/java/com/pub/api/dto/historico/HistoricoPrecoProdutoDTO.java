package com.pub.api.dto.historico;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoricoPrecoProdutoDTO {

	private Long id;
	
	private OffsetDateTime data;
	
	private BigDecimal preco;
	
}
