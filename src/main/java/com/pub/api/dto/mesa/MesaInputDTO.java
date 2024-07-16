package com.pub.api.dto.mesa;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MesaInputDTO {

	@NotNull
	@Min(1)
	private Integer numero;
	
	@NotNull
	@Min(1)
	private Integer capacidade;
	
}
