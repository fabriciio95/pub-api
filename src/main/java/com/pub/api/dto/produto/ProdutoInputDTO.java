package com.pub.api.dto.produto;

import java.math.BigDecimal;

import com.pub.api.dto.categoria.CategoriaIdDTO;
import com.pub.api.dto.unidade.UnidadeIdDTO;
import com.pub.api.dto.unidadeConversao.UnidadeConversaoIdDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoInputDTO {

	@NotBlank
	private String nome;
	
	@NotNull
	@DecimalMin("0.1")
	private BigDecimal preco;
	
	@Min(0)
	private Integer quantidade = 0;
		
	@Valid
	@NotNull
	private CategoriaIdDTO categoria;
	
	@Valid
	@NotNull
	private UnidadeIdDTO unidade;
	
	private UnidadeConversaoIdDTO unidadeConversao;
}
