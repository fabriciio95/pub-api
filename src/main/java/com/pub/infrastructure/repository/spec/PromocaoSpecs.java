package com.pub.infrastructure.repository.spec;


import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import com.pub.domain.model.Promocao;
import com.pub.domain.model.enums.StatusPromocao;

public class PromocaoSpecs {
	
	public static Specification<Promocao> comDataInicioMaiorOuIgualA(LocalDate data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dataInicio"), data.atStartOfDay());
	}
	
	public static Specification<Promocao> comDataFimMenorOuIgualA(LocalDate data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("dataFim"),  data.atTime(LocalTime.MAX));
	}
	
	public static Specification<Promocao> comDataFimMenorQue(LocalDate data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("dataFim"),  data.atTime(LocalTime.MAX));
	}
	
	public static Specification<Promocao> comIdIgualA(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
	}
	
	public static Specification<Promocao> comStatusIgualA(StatusPromocao status) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
	}
	
	public static Specification<Promocao> comDescricaoParecida(String descricao) {
		return (root, query, criteriaBuilder) -> 
				criteriaBuilder.like(criteriaBuilder.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%");
	}
}
