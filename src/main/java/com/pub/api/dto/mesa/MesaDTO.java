package com.pub.api.dto.mesa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.pub.domain.model.enums.StatusMesa;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class MesaDTO {

	private Long id;
	
	private Integer numero;
	
	private Integer capacidade;
	
	private StatusMesa status; 
}
