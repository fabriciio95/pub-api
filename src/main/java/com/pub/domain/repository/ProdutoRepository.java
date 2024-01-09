package com.pub.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	Optional<Produto> findByNomeIgnoreCaseAndUnidadeId(String nome, Long unidadeId);
}
