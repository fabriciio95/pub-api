package com.pub.domain.model;

import java.time.OffsetDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PerdaAvaria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String motivo;
	
	private Integer quantidade;
	
	private OffsetDateTime data;
	
	@ManyToOne
	@JoinColumn(name = "unidade_id")
	private Unidade unidade;
	
	@ManyToOne
	@JoinColumn(name = "produto_id")
	private Produto produto;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PerdaAvaria other = (PerdaAvaria) obj;
		return Objects.equals(id, other.id);
	}
}
