package com.pub.domain.repository.custom;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pub.domain.model.HistoricoProduto;
import com.pub.domain.model.enums.TipoTransacao;

public interface HistoricoProdutoQueries {

	Page<HistoricoProduto> findHistoricoProduto(Long produtoId,LocalDate dataInicio, LocalDate dataFim, TipoTransacao tipoTransacao, Pageable pageable);
}
