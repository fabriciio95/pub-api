package com.pub.domain.model;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import com.pub.domain.model.enums.StatusAtendimento;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Atendimento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private OffsetDateTime dataHoraInicio;
	
	private OffsetDateTime dataHoraFim;
	
	@Enumerated(EnumType.STRING)
	private StatusAtendimento status;
	
	@OneToMany(mappedBy = "atendimento")
	private Set<Pedido> pedidos;
	
	@ManyToMany
	@JoinTable(name = "atendimento_mesa",
	           joinColumns = @JoinColumn(name = "atendimento_id"),
	           inverseJoinColumns = @JoinColumn(name = "mesa_id"))
	private Set<Mesa> mesas = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "atendimento_cliente",
	           joinColumns = @JoinColumn(name = "atendimento_id"),
	           inverseJoinColumns = @JoinColumn(name = "cliente_id"))
	private Set<Cliente> clientes = new HashSet<>();
	
}
