package com.pub.api.dto.cliente;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteInputDTO {
	
	@NotBlank
	private String nome;
	
	@Pattern(regexp = "^\\(?[1-9][0-9]\\)? ?9[6-9][0-9]{3}-?[0-9]{4}$")
	@NotBlank
	private String telefone;
	
	@NotNull
	@CPF
	private String cpf;

}
