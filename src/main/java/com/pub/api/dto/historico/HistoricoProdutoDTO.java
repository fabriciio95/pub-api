package com.pub.api.dto.historico;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.pub.api.dto.perdaavaria.PerdaAvariaDTO;
import com.pub.api.dto.unidade.UnidadeDTO;
import com.pub.domain.model.enums.TipoTransacao;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class HistoricoProdutoDTO {

	private Long id;
	
	private OffsetDateTime data;
	
	private BigDecimal preco;
	
	private Integer quantidade;
	
	private TipoTransacao tipoTransacao;
	
	private UnidadeDTO unidade;
	
	private PerdaAvariaDTO perdaAvaria;

}
