package com.pub.domain.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.exception.ObjetoJaCadastradoException;
import com.pub.domain.model.Produto;
import com.pub.domain.model.enums.TipoTransacao;
import com.pub.domain.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProdutoService {

	private final ProdutoRepository produtoRepository;
	
	private final HistoricoProdutoService historicoProdutoService;
	
	private final HistoricoPrecoProdutoService historicoPrecoProdutoService;
	
	@Transactional
	public Produto salvarProduto(Produto produto) {
		Optional<Produto> produtoOpt = produtoRepository.findByNomeIgnoreCaseAndUnidadeId(produto.getNome(), produto.getUnidade().getId());
		
		if(produtoOpt.isPresent()) {
			Produto produtoSalvo = produtoOpt.get();
			throw new ObjetoJaCadastradoException(String.format("Já existe um produto de nome %s e unidade %s cadastrado",
					produtoSalvo.getNome(), produtoSalvo.getUnidade().getDescricao()));
		}
		
		produto = produtoRepository.save(produto);
		
		historicoPrecoProdutoService.salvarHistoricoPrecoProduto(produto);
		
		if(produto.getQuantidade() != null && produto.getQuantidade() > 0) {
			historicoProdutoService.salvarHistoricoProduto(produto, TipoTransacao.COMPRA, null);
		}
		
		return produto;
	}
}
