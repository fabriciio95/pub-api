package com.pub.infrastructure.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.pub.domain.model.UnidadeConversao;

public class UnidadeConversaoSpecs {

	public static Specification<UnidadeConversao> comDescricaoOrigemParecida(String descricao) {
		return (root, query, criteriaBuilder) -> 
		criteriaBuilder.like(criteriaBuilder.lower(root.get("descricaoOrigem")), "%" + descricao.toLowerCase() + "%");
	}

	public static Specification<UnidadeConversao> comFatorConversaoIgualA(Integer fatorConversao) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("fatorConversao"), fatorConversao) ;
	}
}
