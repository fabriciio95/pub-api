package com.pub.api.dto.promocao.regra;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.pub.api.dto.produto.ProdutoDTO;
import com.pub.domain.model.enums.StatusPromocao;
import com.pub.domain.model.enums.TipoRegraPromocao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegraPromocaoDTO {

	private Long id;
	
	private StatusPromocao status;
	
	private BigDecimal meta;
	
	private BigDecimal valorRegra;
	
	private TipoRegraPromocao tipoRegra;
	
	private ProdutoDTO produtoGratis;
	
	@CreationTimestamp
	private OffsetDateTime dataCadastro;
}
