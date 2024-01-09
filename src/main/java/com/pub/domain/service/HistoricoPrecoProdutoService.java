package com.pub.domain.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.model.HistoricoPrecoProduto;
import com.pub.domain.model.Produto;
import com.pub.domain.repository.HistoricoPrecoProdutoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HistoricoPrecoProdutoService {

	private final HistoricoPrecoProdutoRepository historicoPrecoProdutoRepository;
	
	@Transactional
	public HistoricoPrecoProduto salvarHistoricoPrecoProduto(Produto produto) {
		HistoricoPrecoProduto historicoPrecoProduto = HistoricoPrecoProduto.builder()
				                                                              .data(OffsetDateTime.now())
				                                                              .preco(produto.getPreco())
				                                                              .produto(produto)
				                                                           .build();
		
		return historicoPrecoProdutoRepository.save(historicoPrecoProduto);
	}
}
