package com.pub.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, JpaSpecificationExecutor<Produto> {
	
	Optional<Produto> findByNomeIgnoreCaseAndUnidadeId(String nome, Long unidadeId);
	
	@Query("SELECT CASE WHEN(p.nome = :nome AND p.unidade.id = :unidadeId) THEN true ELSE false END FROM Produto p"
			+ " WHERE p.id != :produtoId AND p.nome = :nome AND p.unidade.id = :unidadeId")
	Boolean isProdutoJaCadastrado(String nome, Long unidadeId, Long produtoId);
	
	@Query("SELECT CASE WHEN(p.ativo = true) THEN true ELSE false END FROM Produto p WHERE p.id = :produtoId")
	Boolean isProdutoAtivo(Long produtoId);
	
	@EntityGraph("Produto.withCategoriaAndUnidade")
    Page<Produto> findAll(Specification<Produto> spec, Pageable pageable);
	
}
