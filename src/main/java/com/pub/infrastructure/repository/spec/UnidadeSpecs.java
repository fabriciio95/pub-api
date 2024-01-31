package com.pub.infrastructure.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.pub.domain.model.Unidade;

public class UnidadeSpecs {

	public static Specification<Unidade> comNomeParecido(String nome) {
		return (root, query, criteriaBuilder) -> 
				criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
	}
}
