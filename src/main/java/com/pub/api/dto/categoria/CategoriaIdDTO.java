package com.pub.api.dto.categoria;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaIdDTO {

	@NotNull
	private Long id;
}
