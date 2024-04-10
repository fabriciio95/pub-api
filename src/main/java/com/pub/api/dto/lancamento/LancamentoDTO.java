package com.pub.api.dto.lancamento;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.pub.api.dto.evento.EventoDTO;
import com.pub.api.dto.historico.HistoricoProdutoDTO;
import com.pub.domain.model.enums.TipoLancamento;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class LancamentoDTO {

	private Long id;
	
	private TipoLancamento tipo;
	
	private String modalidade;
	
	private BigDecimal valor;
	
	private String descricao;
	
	private OffsetDateTime dataCadastro;

	private HistoricoProdutoDTO historicoProduto;
	
	private EventoDTO evento;
}
