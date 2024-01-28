package com.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.HistoricoProduto;
import com.pub.domain.repository.custom.HistoricoProdutoQueries;

@Repository
public interface HistoricoProdutoRepository extends JpaRepository<HistoricoProduto, Long>, HistoricoProdutoQueries {
	
}
