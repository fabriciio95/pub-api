package com.pub.infrastructure.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.pub.domain.model.Produto;

public class ProdutoSpecs {

	public static Specification<Produto> comNomeParecido(String nome) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
	}
	
	public static Specification<Produto> comAtivoIgualA(boolean ativo) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("ativo"), ativo);
	}
	
	public static Specification<Produto> comCategoriaIdIgualA(Long categoriaId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("categoria").get("id"), categoriaId);
	}
	
	public static Specification<Produto> comUnidadeIdIgualA(Long unidadeId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("unidade").get("id"), unidadeId);
	}
	
	public static Specification<Produto> comProdutoIdIgualA(Long produtoId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), produtoId);
	}
}
