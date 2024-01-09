package com.pub.api.dto.unidade;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnidadeIdDTO {

	@NotNull
	private Long id;
}
