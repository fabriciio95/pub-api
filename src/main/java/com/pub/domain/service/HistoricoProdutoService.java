package com.pub.domain.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.model.HistoricoProduto;
import com.pub.domain.model.PerdaAvaria;
import com.pub.domain.model.Produto;
import com.pub.domain.model.enums.TipoTransacao;
import com.pub.domain.repository.HistoricoProdutoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HistoricoProdutoService {

	private final HistoricoProdutoRepository historicoProdutoRepository;
	
	@Transactional
	public HistoricoProduto salvarHistoricoProduto(Produto produto, TipoTransacao tipoTransacao, PerdaAvaria perdaAvaria) {
		HistoricoProduto historicoProduto = criarHistoricoProduto(produto, tipoTransacao, perdaAvaria);
		
		return historicoProdutoRepository.save(historicoProduto);
	}

	private HistoricoProduto criarHistoricoProduto(Produto produto, TipoTransacao tipoTransacao, PerdaAvaria perdaAvaria) {
		return HistoricoProduto.builder()
				   .data(OffsetDateTime.now())
				   .preco(produto.getPreco())
				   .quantidade(produto.getQuantidade())
				   .produto(produto)
				   .unidade(produto.getUnidade())
				   .tipoTransacao(tipoTransacao)
				   .perdaAvaria(perdaAvaria)
             .build();
	}
}
