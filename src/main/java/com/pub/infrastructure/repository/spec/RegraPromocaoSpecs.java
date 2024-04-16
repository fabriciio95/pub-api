package com.pub.infrastructure.repository.spec;


import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.pub.domain.model.RegraPromocao;
import com.pub.domain.model.enums.StatusPromocao;
import com.pub.domain.model.enums.TipoRegraPromocao;

public class RegraPromocaoSpecs {
	
	
	public static Specification<RegraPromocao> comIdIgualA(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
	}
	
	public static Specification<RegraPromocao> comStatusIgualA(StatusPromocao status) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
	}
	
	public static Specification<RegraPromocao> comMetaIgualA(BigDecimal meta) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("meta"), meta);
	}
	
	public static Specification<RegraPromocao> comValorRegraIgualA(BigDecimal valorRegra) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("valorRegra"), valorRegra);
	}
	
	public static Specification<RegraPromocao> comTipoRegraIgualA(TipoRegraPromocao tipoRegra) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tipoRegra"), tipoRegra);
	}
	
	public static Specification<RegraPromocao> comProdutoGratisIdIgualA(Long produtoGratisId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("produtoGratis").get("id"), produtoGratisId);
	}
}
