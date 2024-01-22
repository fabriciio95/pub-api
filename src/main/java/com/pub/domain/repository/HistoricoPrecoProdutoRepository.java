package com.pub.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.HistoricoPrecoProduto;

@Repository
public interface HistoricoPrecoProdutoRepository extends JpaRepository<HistoricoPrecoProduto, Long> {
	
	Page<HistoricoPrecoProduto> findById(Long produtoId, Pageable pageable);
}
