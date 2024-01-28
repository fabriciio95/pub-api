package com.pub.domain.service;

import static com.pub.infrastructure.repository.spec.ProdutoSpecs.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.exception.EntidadeNaoEncontradaException;
import com.pub.domain.exception.ObjetoJaCadastradoException;
import com.pub.domain.exception.ViolacaoRegraNegocioException;
import com.pub.domain.model.PerdaAvaria;
import com.pub.domain.model.Produto;
import com.pub.domain.model.Unidade;
import com.pub.domain.model.UnidadeConversao;
import com.pub.domain.model.enums.TipoTransacao;
import com.pub.domain.repository.ProdutoRepository;
import com.pub.domain.repository.UnidadeRepository;
import com.pub.domain.service.dto.TransacaoEstoqueDTO;

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
	
	private final PerdaAvariaService perdaAvariaService;
	
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
		produto.setQuantidade(produto.getQuantidade() == null ? 0 : produto.getQuantidade());
		
		produto = produtoRepository.save(produto);
		
		historicoPrecoProdutoService.salvarHistoricoPrecoProduto(produto);
		
		if(produto.getQuantidade() > 0) {
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
	public void registrarAlteracaoEstoque(TransacaoEstoqueDTO transacaoEstoqueDTO) {
		Produto produto = findProdutoById(transacaoEstoqueDTO.getProdutoId());
		
		if(!produtoRepository.isProdutoAtivo(transacaoEstoqueDTO.getProdutoId()))
			throw new ViolacaoRegraNegocioException(String.format("Não foi possível registrar alteração de estoque pois o produto de código %d está inativo", transacaoEstoqueDTO.getProdutoId()));
		
		TipoTransacao tipoTransacao = TipoTransacao.findTipoTransacao(transacaoEstoqueDTO.getTipoTransacao());
		
		if(transacaoEstoqueDTO.getQuantidade() == null || transacaoEstoqueDTO.getQuantidade() <= 0)
			throw new ViolacaoRegraNegocioException("Quantidade informada inválida");
		
		if(isTransacaoPerdaAvariaInvalida(transacaoEstoqueDTO, tipoTransacao)) {
			throw new ViolacaoRegraNegocioException("Para registro de alteração de estoque de perdas ou avarias é necessário informar o motivo");
		} 
		
		if(tipoTransacao.equals(TipoTransacao.COMPRA) && transacaoEstoqueDTO.getPerdaAvariaDTO() != null) {
			throw new ViolacaoRegraNegocioException("Para registro de compra não pode ser informado motivo de perda ou avaria");
		}
		
		if(tipoTransacao.equals(TipoTransacao.VENDA)) {
			throw new ViolacaoRegraNegocioException("Tipo de transação inválida, registros de vendas é calculado de forma automática");
		}
		
		boolean converterQuantidade = true;
		
		if(transacaoEstoqueDTO.getUnidadeConversaoId() == null) 
			converterQuantidade = false;
		
		UnidadeConversao unidadeConversao = null;
		
		Integer novaQuantidade = null;
		
		if(converterQuantidade) {
		
			unidadeConversao = produto.getUnidade().getUnidadesConversao().stream()
		                                            .filter(uc -> uc.getId().equals(transacaoEstoqueDTO.getUnidadeConversaoId()))
		                                            .findFirst()
		                                            .orElseThrow(() -> new ViolacaoRegraNegocioException(String.format("Unidade de conversão informada não vinculada"
		                                            		+ " a unidade do produto %s de código %d", produto.getNome(), produto.getId())));
			
			novaQuantidade = calcularQuantidadeProduto(produto.getQuantidade(), transacaoEstoqueDTO.getQuantidade(), tipoTransacao, unidadeConversao);
		} else {
			novaQuantidade = calcularQuantidadeProduto(produto.getQuantidade(), transacaoEstoqueDTO.getQuantidade(), tipoTransacao);
		}
		
		PerdaAvaria perdaAvaria = null;
		
		Integer quantidadeTransacao =  Math.abs(produto.getQuantidade() - novaQuantidade);
		
		if(tipoTransacao.equals(TipoTransacao.PERDA)) {
			
			perdaAvaria = PerdaAvaria.builder()
	  								  .data(OffsetDateTime.now())
	  								  .motivo(transacaoEstoqueDTO.getPerdaAvariaDTO().getMotivo())
	  								  .produto(produto)
	  								  .quantidade(quantidadeTransacao)
	  								  .unidade(produto.getUnidade())
	                                 .build();
			
			if(!isProdutoTemEstoqueDisponivel(produto.getQuantidade(), quantidadeTransacao)) {
				throw new ViolacaoRegraNegocioException(String.format(" Produto de código %d sem estoque disponível", transacaoEstoqueDTO.getProdutoId()));
			}
			
			perdaAvaria =  perdaAvariaService.salvarPerdaAvaria(perdaAvaria);
		}
		
		produto.setQuantidade(novaQuantidade);
		
		historicoProdutoService.salvarHistoricoProduto(produto, tipoTransacao, perdaAvaria, quantidadeTransacao);
	}
	
	public Page<Produto> pesquisarProdutos(Long produtoId, String nome, Boolean ativo, Long categoriaId, Long unidadeId, Pageable pageable) {
		
		Specification<Produto> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
		
		if(produtoId != null) 
			spec = spec.and(comProdutoIdIgualA(produtoId));
		
		if(nome != null)
			spec = spec.and(comNomeParecido(nome));
		
		if(categoriaId != null)
			spec = spec.and(comCategoriaIdIgualA(categoriaId));
		
		if(unidadeId != null)
			spec = spec.and(comUnidadeIdIgualA(unidadeId));
		
		if(ativo != null)
			spec = spec.and(comAtivoIgualA(ativo));
		
		return this.produtoRepository.findAll(spec, pageable);
	}

	private boolean isTransacaoPerdaAvariaInvalida(TransacaoEstoqueDTO transacaoEstoqueDTO, TipoTransacao tipoTransacao) {
		return (tipoTransacao.equals(TipoTransacao.PERDA) && transacaoEstoqueDTO.getPerdaAvariaDTO() == null) || 
				(tipoTransacao.equals(TipoTransacao.PERDA) && transacaoEstoqueDTO.getPerdaAvariaDTO() != null && transacaoEstoqueDTO.getPerdaAvariaDTO().getMotivo() == null);
	}

	private Integer calcularQuantidadeProduto(Integer quantidadeAtual, Integer quantidadeInformada, TipoTransacao tipoTransacao) {
		Integer novaQuantidade = 0;
		
		if(TipoTransacao.COMPRA.equals(tipoTransacao)) {
			novaQuantidade = quantidadeAtual + quantidadeInformada;
		} else {
			novaQuantidade = quantidadeAtual - quantidadeInformada;
		}
		
		return novaQuantidade;
	}
	
	private Integer calcularQuantidadeProduto(Integer quantidadeAtual, Integer quantidadeInformada, TipoTransacao tipoTransacao, UnidadeConversao unidadeConversao) {
		
	    Integer quantidadeConvertida = quantidadeInformada * unidadeConversao.getFatorConversao();
	    
		return calcularQuantidadeProduto(quantidadeAtual, quantidadeConvertida, tipoTransacao);
	}
	
	private boolean isProdutoTemEstoqueDisponivel(Integer quantidadeAtual, Integer quantidadeTransacao) {
		return quantidadeAtual != null && (quantidadeAtual - quantidadeTransacao >= 0);
	}

	@Transactional
	public Produto findProdutoById(Long produtoId) {
		return produtoRepository.findById(produtoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Produto de código %d não encontrado", produtoId)));
	}
}
