package com.pub.domain.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	private String descricao;
	
	@ManyToMany
	@JoinTable(name = "categoria_unidade_conversao",
	      joinColumns = @JoinColumn(name = "categoria_id"),
	      inverseJoinColumns = @JoinColumn(name = "unidade_conversao_id"))
	private Set<UnidadeConversao> unidadesConversao = new HashSet<>();
	
	public void adicionarUnidadeConversao(UnidadeConversao unidadeConversao) {
		this.unidadesConversao.add(unidadeConversao);
	}
	
	public void removerUnidadeConversao(UnidadeConversao unidadeConversao) {
		this.unidadesConversao.remove(unidadeConversao);
	}

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
		Categoria other = (Categoria) obj;
		return Objects.equals(id, other.id);
	}
}
