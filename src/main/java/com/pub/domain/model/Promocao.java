package com.pub.domain.model;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.pub.domain.model.enums.StatusPromocao;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Promocao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private OffsetDateTime dataInicio;
	
	private OffsetDateTime dataFim;
	
	@Enumerated(EnumType.STRING)
	private StatusPromocao status;

	private String descricao;
	
	private boolean aplicarTodasRegras;
	
	@CreationTimestamp
	private OffsetDateTime dataCadastro;
	
	@UpdateTimestamp
	private OffsetDateTime dataAtualizacao;
	
	@OneToMany(mappedBy = "id")
	private Set<RegraPromocao> regras = new HashSet<>();
}
