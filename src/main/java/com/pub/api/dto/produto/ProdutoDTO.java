package com.pub.api.dto.produto;

import java.math.BigDecimal;

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
	
	private CategoriaIdDTO categoria;
	
	private UnidadeIdDTO unidade;
	
}
