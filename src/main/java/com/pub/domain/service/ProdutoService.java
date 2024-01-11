package com.pub.domain.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.exception.EntidadeNaoEncontradaException;
import com.pub.domain.exception.ObjetoJaCadastradoException;
import com.pub.domain.exception.ViolacaoRegraNegocioException;
import com.pub.domain.model.Produto;
import com.pub.domain.model.Unidade;
import com.pub.domain.model.enums.TipoTransacao;
import com.pub.domain.repository.ProdutoRepository;
import com.pub.domain.repository.UnidadeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProdutoService {

	private final ProdutoRepository produtoRepository;
	
	private final HistoricoProdutoService historicoProdutoService;
	
	private final HistoricoPrecoProdutoService historicoPrecoProdutoService;
	
	private final UnidadeRepository unidadeRepository;
	
	private final CategoriaService categoriaService;
	
	private final UnidadeService unidadeService;
	
	@Transactional
	public Produto cadastrarProduto(Produto produto) {
        unidadeService.existsUnidadeById(produto.getUnidade().getId());
		
		categoriaService.existsCategoriaById(produto.getCategoria().getId());
		
		Optional<Produto> produtoOpt = produtoRepository.findByNomeIgnoreCaseAndUnidadeId(produto.getNome(), produto.getUnidade().getId());
		
		if(produtoOpt.isPresent()) {
			Produto produtoSalvo = produtoOpt.get();
			throw new ObjetoJaCadastradoException(String.format("Já existe um produto de nome %s e unidade %s cadastrado",
					produtoSalvo.getNome(), produtoSalvo.getUnidade().getDescricao()));
		}
		
		produto.setAtivo(true);
		
		produto = produtoRepository.save(produto);
		
		historicoPrecoProdutoService.salvarHistoricoPrecoProduto(produto);
		
		if(produto.getQuantidade() != null && produto.getQuantidade() > 0) {
			historicoProdutoService.salvarHistoricoProduto(produto, TipoTransacao.COMPRA, null);
		}
		
		return produto;
	}
	
	@Transactional
	public Produto atualizarProduto(Produto produto, Long produtoId) {
		Produto produtoSalvo = findProdutoById(produtoId);
		
		if(!produtoRepository.isProdutoAtivo(produtoId))
			throw new ViolacaoRegraNegocioException(String.format("O produto %s de código %d não pode ser editado pois está inativo", produtoSalvo.getNome(), produtoId));
		
		categoriaService.existsCategoriaById(produto.getCategoria().getId());
		
		Unidade unidade = unidadeRepository.findById(produto.getUnidade().getId())
				.orElseThrow(() -> new ViolacaoRegraNegocioException(String.format("Unidade de código %d não cadastrada", produto.getUnidade().getId())));
		
		Boolean isProdutoJaCadastrado = produtoRepository.isProdutoJaCadastrado(produto.getNome(), produto.getUnidade().getId(), produtoId);
	
		if(isProdutoJaCadastrado != null && isProdutoJaCadastrado) {
			throw new ObjetoJaCadastradoException(String.format("Já existe um produto de nome %s e unidade %s cadastrado",
					produto.getNome(), unidade.getDescricao()));
		}
		
		if(produto.getQuantidade() != produtoSalvo.getQuantidade()) {
			throw new ViolacaoRegraNegocioException(String.format("A quantidade do produto %s não pode ser alterada ao editar o produto, é necessário um registro de alteração de estoque do produto para isso",
					produtoSalvo.getNome()));
		}
		
		BigDecimal precoAtual = produtoSalvo.getPreco();
		
		BeanUtils.copyProperties(produto, produtoSalvo, "id", "dataCadastro", "dataAtualizacao", "quantidade", "ativo");
		
		if(produto.getPreco().compareTo(precoAtual) != 0) {
			historicoPrecoProdutoService.salvarHistoricoPrecoProduto(produtoSalvo);
		}
		
		return produtoSalvo;
	}
	
	@Transactional
	public void alterarStatusAtivacaoProduto(Long produtoId, boolean ativo) {
	    Produto produto = findProdutoById(produtoId);
	    
	    produto.setAtivo(ativo);
	}

	@Transactional
	private Produto findProdutoById(Long produtoId) {
		return produtoRepository.findById(produtoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Produto de código %d não encontrado", produtoId)));
	}
}
