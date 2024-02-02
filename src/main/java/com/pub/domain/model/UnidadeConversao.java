package com.pub.domain.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UnidadeConversao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String descricaoOrigem;
	
	private Integer fatorConversao;
	
	@ManyToMany(mappedBy = "unidadesConversao")
    private Set<Categoria> categorias = new HashSet<>();

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
		UnidadeConversao other = (UnidadeConversao) obj;
		return Objects.equals(id, other.id);
	}
}
