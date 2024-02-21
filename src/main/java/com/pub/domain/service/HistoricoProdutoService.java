package com.pub.domain.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.model.HistoricoProduto;
import com.pub.domain.model.Produto;
import com.pub.domain.model.enums.TipoTransacao;
import com.pub.domain.repository.HistoricoProdutoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HistoricoProdutoService {

	private final HistoricoProdutoRepository historicoProdutoRepository;
	
	@Transactional
	public HistoricoProduto salvarHistoricoProduto(HistoricoProduto historicoProduto) {
		return historicoProdutoRepository.save(historicoProduto);
	}
	
	public Page<HistoricoProduto> listarHistoricoProduto(Produto produto, Pageable pageable, LocalDate dataInicio, LocalDate dataFim, TipoTransacao tipoTransacao) {
		return historicoProdutoRepository.findHistoricoProduto(produto.getId(), dataInicio, dataFim, tipoTransacao, pageable);
	}
}
