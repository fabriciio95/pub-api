package com.pub.domain.model;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pub.domain.model.enums.StatusPedido;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private OffsetDateTime dataHora;
	
	@Enumerated(EnumType.STRING)
	private StatusPedido status;
	
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	private List<ItemPedido> items;
	
	@ManyToMany
	@JoinTable(name = "funcionario_pedido",
	           joinColumns = @JoinColumn(name = "pedido_id"),
	           inverseJoinColumns = @JoinColumn(name = "funcionario_id"))
	private Set<Funcionario> funcionarios = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name = "atendimento_id")
	private Atendimento atendimento;
}
