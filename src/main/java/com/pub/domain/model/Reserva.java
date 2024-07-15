package com.pub.domain.model;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import com.pub.domain.model.enums.StatusReserva;

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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private OffsetDateTime dataHoraInicio;
	
	private OffsetDateTime dataHoraFim;
	
	private Integer quantidadePessoas;
	
	@Enumerated(EnumType.STRING)
	private StatusReserva status;
	
	@ManyToMany
	@JoinTable(name = "reserva_mesa",
	           joinColumns = @JoinColumn(name = "reserva_id"),
	           inverseJoinColumns = @JoinColumn(name = "mesa_id"))
	private Set<Mesa> mesas = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
}
