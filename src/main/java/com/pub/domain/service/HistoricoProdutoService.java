package com.pub.domain.service;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	@Transactional
	public HistoricoProduto salvarHistoricoProduto(Produto produto, TipoTransacao tipoTransacao, PerdaAvaria perdaAvaria, Integer quantidade) {
		HistoricoProduto historicoProduto = criarHistoricoProduto(produto, tipoTransacao, quantidade, perdaAvaria);
		
		return historicoProdutoRepository.save(historicoProduto);
	}
	
	public Page<HistoricoProduto> listarHistoricoProduto(Long produtoId, Pageable pageable) {
		return historicoProdutoRepository.findById(produtoId, pageable);
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
	
	private HistoricoProduto criarHistoricoProduto(Produto produto, TipoTransacao tipoTransacao, Integer quantidade, PerdaAvaria perdaAvaria) {
		return HistoricoProduto.builder()
				   .data(OffsetDateTime.now())
				   .preco(produto.getPreco())
				   .quantidade(quantidade)
				   .produto(produto)
				   .unidade(produto.getUnidade())
				   .tipoTransacao(tipoTransacao)
				   .perdaAvaria(perdaAvaria)
             .build();
	}
}
