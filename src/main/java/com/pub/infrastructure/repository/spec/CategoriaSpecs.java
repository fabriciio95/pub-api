package com.pub.infrastructure.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.pub.domain.model.Categoria;

public class CategoriaSpecs {

	public static Specification<Categoria> comNomeParecido(String nome) {
		return (root, query, criteriaBuilder) -> 
				criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
	}
}
