package com.pub.api.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaInputDTO {

	@NotBlank
	private String nome;
	
	private String descricao;
}
