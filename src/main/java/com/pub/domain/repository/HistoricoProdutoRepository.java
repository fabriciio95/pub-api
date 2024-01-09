package com.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.HistoricoProduto;

@Repository
public interface HistoricoProdutoRepository extends JpaRepository<HistoricoProduto, Long> {
	
}
