package com.pub.domain.service;

import static com.pub.infrastructure.repository.spec.ProdutoSpecs.comAtivoIgualA;
import static com.pub.infrastructure.repository.spec.ProdutoSpecs.comCategoriaIdIgualA;
import static com.pub.infrastructure.repository.spec.ProdutoSpecs.comNomeParecido;
import static com.pub.infrastructure.repository.spec.ProdutoSpecs.comProdutoIdIgualA;
import static com.pub.infrastructure.repository.spec.ProdutoSpecs.comUnidadeIdIgualA;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.pub.domain.model.HistoricoProduto;
import com.pub.domain.model.Lancamento;
import com.pub.domain.model.PerdaAvaria;
import com.pub.domain.model.Produto;
import com.pub.domain.model.Unidade;
import com.pub.domain.model.UnidadeConversao;
import com.pub.domain.model.enums.ModalidadeLancamento;
import com.pub.domain.model.enums.TipoLancamento;
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
	
	private final LancamentoService lancamentoService;
	
	@Transactional
	public Produto cadastrarProduto(Produto produto, Long unidadeConversaoId, BigDecimal valorTotal) {
        unidadeService.existsUnidadeById(produto.getUnidade().getId());
		
        try {
        	
        	produto.setCategoria(categoriaService.findCategoriaById(produto.getCategoria().getId()));
        	
        } catch(EntidadeNaoEncontradaException ex) {
			throw new ViolacaoRegraNegocioException(String.format("Categoria de código %d não cadastrada", produto.getCategoria().getId()));
        }
		
		Optional<Produto> produtoOpt = produtoRepository.findByNomeIgnoreCaseAndUnidadeId(produto.getNome(), produto.getUnidade().getId());
		
		if(produtoOpt.isPresent()) {
			Produto produtoSalvo = produtoOpt.get();
			throw new ObjetoJaCadastradoException(String.format("Já existe um produto de nome %s e unidade %s cadastrado",
					produtoSalvo.getNome(), produtoSalvo.getUnidade().getNome()));
		}
		
		produto.setAtivo(true);
		
		Integer quantidadeInformada = produto.getQuantidade() != null ? produto.getQuantidade() : 0;
		
		produto.setQuantidade(0);
		
		produto.setQuantidade(calcularQuantidadeEstoqueProduto(produto, unidadeConversaoId, quantidadeInformada, TipoTransacao.COMPRA));
		
		produto = produtoRepository.save(produto);
		
		historicoPrecoProdutoService.salvarHistoricoPrecoProduto(produto);
		
		if(produto.getQuantidade() > 0) {		
			if(valorTotal == null || valorTotal.compareTo(BigDecimal.ZERO) == 0) {
				throw new ViolacaoRegraNegocioException("Para registro de quantidade de estoque deve ser informado um valor total maior do que zero");
			}
			
			HistoricoProduto historicoProduto = HistoricoProduto.builder()
				   .data(OffsetDateTime.now())
				   .valorTotal(valorTotal)
				   .valorUnitario(valorTotal.divide(new BigDecimal(produto.getQuantidade()), 2, RoundingMode.HALF_UP))
				   .quantidadeTransacao(produto.getQuantidade())
				   .quantidadeEstoque(produto.getQuantidade())
				   .produto(produto)
				   .unidade(produto.getUnidade())
				   .tipoTransacao(TipoTransacao.COMPRA)
			   .build();
			
			historicoProdutoService.salvarHistoricoProduto(historicoProduto);
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
					produto.getNome(), unidade.getNome()));
		}
		
		if(!produtoSalvo.getQuantidade().equals(produto.getQuantidade())) {
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
		
		if(tipoTransacao.equals(TipoTransacao.COMPRA) && (transacaoEstoqueDTO.getValorTotal() == null || transacaoEstoqueDTO.getValorTotal().compareTo(BigDecimal.ZERO) == 0)) {
			throw new ViolacaoRegraNegocioException("Para registro de compra deve ser informado um valor total maior do que zero");
		}
		
		if(tipoTransacao.equals(TipoTransacao.VENDA)) {
			throw new ViolacaoRegraNegocioException("Tipo de transação inválida, registros de vendas é calculado de forma automática");
		}
		
		Integer novaQuantidade = calcularQuantidadeEstoqueProduto(produto, transacaoEstoqueDTO.getUnidadeConversaoId(), 
				transacaoEstoqueDTO.getQuantidade(), tipoTransacao);
		
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
				throw new ViolacaoRegraNegocioException(String.format("Produto de código %d sem estoque suficiente disponível", transacaoEstoqueDTO.getProdutoId()));
			}
			
			transacaoEstoqueDTO.setValorTotal(null);
			
			perdaAvaria =  perdaAvariaService.salvarPerdaAvaria(perdaAvaria);
		}
		
		produto.setQuantidade(novaQuantidade);
		
		HistoricoProduto historicoProduto = HistoricoProduto.builder()
				   .data(OffsetDateTime.now())
				   .valorTotal(transacaoEstoqueDTO.getValorTotal())
				   .valorUnitario(calcularPrecoUnitarioProduto(transacaoEstoqueDTO, produto, tipoTransacao))
				   .quantidadeTransacao(quantidadeTransacao)
				   .quantidadeEstoque(produto.getQuantidade())
				   .produto(produto)
				   .unidade(produto.getUnidade())
				   .tipoTransacao(tipoTransacao)
				   .perdaAvaria(perdaAvaria)
			   .build();
		
		historicoProdutoService.salvarHistoricoProduto(historicoProduto);
		
		if(tipoTransacao.equals(TipoTransacao.COMPRA)) {
			Lancamento lancamento = Lancamento.builder()
					                          	 .descricao("Reposição de estoque para o produto " + produto.getNome())
					                          	 .historicoProduto(historicoProduto)
					                          	 .modalidade(ModalidadeLancamento.ALTERACAO_ESTOQUE)
					                          	 .tipo(TipoLancamento.DESPESA)
					                          	 .valor(transacaoEstoqueDTO.getValorTotal())
											  .build();
			
			lancamentoService.cadastrarLancamento(lancamento);
		}
	}

	private UnidadeConversao obterUnidadeConversao(Long unidadeConversaoId, Produto produto) {
		return produto.getCategoria().getUnidadesConversao().stream()
		                                        .filter(uc -> uc.getId().equals(unidadeConversaoId))
		                                        .findFirst()
		                                        .orElseThrow(() -> new ViolacaoRegraNegocioException(String.format("Unidade de conversão informada não vinculada"
		                                        		+ " a categoria do produto %s de código %d", produto.getNome(), produto.getId())));
	}
	
	private Integer calcularQuantidadeEstoqueProduto(Produto produto, Long idUnidadeConversao, Integer quantidadeTransacao, TipoTransacao tipoTransacao) {
		if(quantidadeTransacao == null || quantidadeTransacao == 0)
			return 0;
		
		boolean converterQuantidade = true;
		
		if(idUnidadeConversao == null) 
			converterQuantidade = false;
		
		UnidadeConversao unidadeConversao = null;
		
		if(converterQuantidade) {
			
			unidadeConversao = obterUnidadeConversao(idUnidadeConversao, produto);
		
			Integer quantidadeConvertida = converterQuantidade(quantidadeTransacao, unidadeConversao);
			
			return calcularQuantidadeProduto(produto.getQuantidade(), quantidadeConvertida, tipoTransacao);
		} 
			
		return calcularQuantidadeProduto(produto.getQuantidade(), quantidadeTransacao, tipoTransacao);
	}
	
	private BigDecimal calcularPrecoUnitarioProduto(TransacaoEstoqueDTO transacaoEstoqueDTO, Produto produto, TipoTransacao tipoTransacao) {
		if(tipoTransacao.equals(TipoTransacao.PERDA))
			return null;
		
		if(transacaoEstoqueDTO.getUnidadeConversaoId() == null)
			return transacaoEstoqueDTO.getValorTotal().divide(new BigDecimal(transacaoEstoqueDTO.getQuantidade()));
		
		UnidadeConversao unidadeConversao = obterUnidadeConversao(transacaoEstoqueDTO.getUnidadeConversaoId(), produto);
		
		Integer quantidadeConvertida = converterQuantidade(transacaoEstoqueDTO.getQuantidade(), unidadeConversao);
		
		return transacaoEstoqueDTO.getValorTotal().divide(new BigDecimal(quantidadeConvertida), 2, RoundingMode.HALF_UP);
	}
	
	private Integer converterQuantidade(Integer quantidade, UnidadeConversao unidadeConversao) {
		return quantidade * unidadeConversao.getFatorConversao();
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
	
	private boolean isProdutoTemEstoqueDisponivel(Integer quantidadeAtual, Integer quantidadeTransacao) {
		return quantidadeAtual != null && (quantidadeAtual - quantidadeTransacao >= 0);
	}

	@Transactional
	public Produto findProdutoById(Long produtoId) {
		return produtoRepository.findById(produtoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Produto de código %d não encontrado", produtoId)));
	}
}
