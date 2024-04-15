package com.pub.api.dto.promocao;

import java.time.OffsetDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.pub.api.dto.promocao.regra.RegraPromocaoDTO;
import com.pub.domain.model.enums.StatusPromocao;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class PromocaoDTO {

	private Long id;
	
	private OffsetDateTime dataInicio;
	
	private OffsetDateTime dataFim;
	
	private StatusPromocao status;

	private String descricao;
	
	private boolean aplicarTodasRegras;
	
	private OffsetDateTime dataCadastro;
	
	private Set<RegraPromocaoDTO> regras;
}
