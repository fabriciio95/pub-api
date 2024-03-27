package com.pub.infrastructure.repository.spec;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import com.pub.domain.model.Lancamento;
import com.pub.domain.model.enums.ModalidadeLancamento;
import com.pub.domain.model.enums.TipoLancamento;

public class LancamentoSpecs {

	public static Specification<Lancamento> comDataMaiorOuIgualA(LocalDate data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dataCadastro"), data.atStartOfDay());
	}
	
	public static Specification<Lancamento> comDataMenorOuIgualA(LocalDate data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("dataCadastro"), data.atTime(LocalTime.MAX));
	}
	
	public static Specification<Lancamento> comIdIgualA(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
	} 
	
	public static Specification<Lancamento> comTipoIgualA(TipoLancamento tipo) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tipo"), tipo);
	} 
	
	public static Specification<Lancamento> comModalidadeIgualA(ModalidadeLancamento modalidade) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("modalidade"), modalidade);
	} 
	
	public static Specification<Lancamento> comProdutoIdIgualA(Long produtoId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("historicoProduto").get("produto").get("id"), produtoId);
	}
	
	public static Specification<Lancamento> comDescricaoParecida(String descricao) {
		return (root, query, criteriaBuilder) -> 
				criteriaBuilder.like(criteriaBuilder.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%");
	}
}
