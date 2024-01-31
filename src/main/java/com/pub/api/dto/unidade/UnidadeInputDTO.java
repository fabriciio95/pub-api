package com.pub.api.dto.unidade;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnidadeInputDTO {

	@NotBlank
	private String nome;
}
