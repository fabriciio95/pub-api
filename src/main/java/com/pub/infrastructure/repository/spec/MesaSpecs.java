package com.pub.infrastructure.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.pub.domain.model.Mesa;
import com.pub.domain.model.enums.StatusMesa;

public class MesaSpecs {

	public static Specification<Mesa> comNumeroIgualA(Integer numero) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("numero"), numero);
	}
	
	public static Specification<Mesa> comCapacidadeIgualA(Integer capacidade) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("capacidade"), capacidade);
	}
	
	public static Specification<Mesa> comStatusIgualA(StatusMesa status) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
	}
	
	public static Specification<Mesa> comMesaIdIgualA(Long mesaId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), mesaId);
	}
}
