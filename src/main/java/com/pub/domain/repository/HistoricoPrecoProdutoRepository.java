package com.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.HistoricoPrecoProduto;

@Repository
public interface HistoricoPrecoProdutoRepository extends JpaRepository<HistoricoPrecoProduto, Long> {
	
}
