package com.pub.api.dto.produto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.pub.api.dto.categoria.CategoriaIdDTO;
import com.pub.api.dto.unidade.UnidadeIdDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoDTO {

	private Long id;
	
	private String nome;
	
	private BigDecimal preco;
	
	private Integer quantidade;
	
	private boolean ativo;
	
	private OffsetDateTime dataCadastro;
	
	private CategoriaIdDTO categoria;
	
	private UnidadeIdDTO unidade;
	
}
