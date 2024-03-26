package com.pub.api.dto.lancamento;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.pub.api.dto.historico.HistoricoProdutoDTO;
import com.pub.domain.model.enums.TipoLancamento;

import lombok.Getter;
import lombok.Setter;

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
}
