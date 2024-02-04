package com.pub.api.dto.unidadeConversao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnidadeConversaoInputDTO {
	
	@NotBlank
	private String descricaoOrigem;
	
	@NotNull
	@Min(1)
	private Integer fatorConversao;
}
