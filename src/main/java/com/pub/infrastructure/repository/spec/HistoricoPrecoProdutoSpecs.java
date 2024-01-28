package com.pub.infrastructure.repository.spec;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import com.pub.domain.model.HistoricoPrecoProduto;

public class HistoricoPrecoProdutoSpecs {

	public static Specification<HistoricoPrecoProduto> comDataMaiorOuIgualA(LocalDate data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("data"), data.atStartOfDay());
	}
	
	public static Specification<HistoricoPrecoProduto> comDataMenorOuIgualA(LocalDate data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("data"), data.atTime(LocalTime.MAX));
	}
	
	public static Specification<HistoricoPrecoProduto> comProdutoIdIgualA(Long produtoId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("produto").get("id"), produtoId);
	} 
}
