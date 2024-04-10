package com.pub.domain.model;

import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.pub.domain.model.enums.StatusEvento;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Evento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	private StatusEvento status;
	
	private OffsetDateTime dataHoraInicioEvento;
	
	private OffsetDateTime dataHoraFimEvento;
	
	private OffsetDateTime dataHoraCancelamento;
	
	private OffsetDateTime dataHoraFinalizacao;
	
	@CreationTimestamp
	private OffsetDateTime dataCadastro;
	
	@UpdateTimestamp
	private OffsetDateTime dataAtualizacao;
}
