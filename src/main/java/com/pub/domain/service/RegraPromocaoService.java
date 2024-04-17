package com.pub.domain.service;

import static com.pub.infrastructure.repository.spec.RegraPromocaoSpecs.comIdIgualA;
import static com.pub.infrastructure.repository.spec.RegraPromocaoSpecs.comMetaIgualA;
import static com.pub.infrastructure.repository.spec.RegraPromocaoSpecs.comProdutoGratisIdIgualA;
import static com.pub.infrastructure.repository.spec.RegraPromocaoSpecs.comStatusIgualA;
import static com.pub.infrastructure.repository.spec.RegraPromocaoSpecs.comTipoRegraIgualA;
import static com.pub.infrastructure.repository.spec.RegraPromocaoSpecs.comValorRegraIgualA;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.exception.EntidadeNaoEncontradaException;
import com.pub.domain.exception.ObjetoConflitanteException;
import com.pub.domain.exception.ViolacaoRegraNegocioException;
import com.pub.domain.model.Produto;
import com.pub.domain.model.Promocao;
import com.pub.domain.model.RegraPromocao;
import com.pub.domain.model.enums.StatusPromocao;
import com.pub.domain.model.enums.TipoRegraPromocao;
import com.pub.domain.repository.RegraPromocaoRepository;
import com.pub.domain.service.dto.RegraPromocaoFiltroDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RegraPromocaoService {

	private final RegraPromocaoRepository regraPromocaoRepository;
	
	private final PromocaoService promocaoService;
	
	private final ProdutoService produtoService;
	
	
	@Transactional
	public Page<RegraPromocao> pesquisar(RegraPromocaoFiltroDTO filtro, Pageable pageable) {
		
		Specification<RegraPromocao> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
		
		if(filtro.getMeta() != null) {
			spec = spec.and(comMetaIgualA(filtro.getMeta()));
		}
		
		if(filtro.getProdutoGratisId() != null) {
			spec = spec.and(comProdutoGratisIdIgualA(filtro.getProdutoGratisId()));
		}
		
		if(filtro.getRegraId() != null) {
			spec = spec.and(comIdIgualA(filtro.getRegraId()));
		}
		
		if(filtro.getStatus() != null) {
			spec = spec.and(comStatusIgualA(filtro.getStatus()));
		}
		
		if(filtro.getTipoRegra() != null) {
			spec = spec.and(comTipoRegraIgualA(filtro.getTipoRegra()));
		}
		
		if(filtro.getValorRegra() != null) {
			spec = spec.and(comValorRegraIgualA(filtro.getValorRegra()));
		}
		
		return regraPromocaoRepository.findAll(spec, pageable);
	}
	
	@Transactional
	public Page<Produto> getProdutosRegra(Long promocaoId, Long regraId, Pageable pageable) {
		RegraPromocao regra = findRegraById(promocaoId, regraId);
		
		return regraPromocaoRepository.findProdutosByRegraPromocaoId(regra.getId(), pageable);
	}
	
	@Transactional
	public RegraPromocao cadastrar(RegraPromocao regra, Long promocaoId) {
		Promocao promocao = promocaoService.findPromocaoById(promocaoId);
		
		regra.setPromocao(promocao);
		regra.setStatus(StatusPromocao.ATIVA);
		
		validarRegra(regra);
		
		return regraPromocaoRepository.save(regra);
		
	}
	
	@Transactional
	public RegraPromocao atualizar(RegraPromocao regra, Long regraId, Long promocaoId) {
		RegraPromocao regraCadastrada = findRegraById(promocaoId, regraId);
		
		validarRegra(regra);
		
		BeanUtils.copyProperties(regra, regraCadastrada, "id", "dataCadastro", "dataAtualizacao", "status");
		
		return regraCadastrada;
	}
	
	@Transactional
	public void alterarStatus(StatusPromocao status, Long promocaoId, Long regraId) {
		RegraPromocao regra = findRegraById(promocaoId, regraId);
		
		regra.setStatus(status);
	}
	
	@Transactional
	public void associarProdutos(Long promocaoId, Long regraId, List<Long> produtosId) {
		RegraPromocao regra = findRegraById(promocaoId, regraId);
		
		produtosId.forEach(produtoId -> {
			try {
				
				Produto produto = produtoService.findProdutoById(produtoId);
				
				regra.adicionarProduto(produto);
				
			} catch(EntidadeNaoEncontradaException ex) {
				throw new ObjetoConflitanteException(String.format("Produto de código %d não cadastrado", produtoId), ex);
			}
		});
	}
	
	@Transactional
	public void desassociarProdutos(Long promocaoId, Long regraId, List<Long> produtosId) {
		
		RegraPromocao regra = findRegraById(promocaoId, regraId);
		
		produtosId.forEach(produtoId -> {
			try {
				
				Produto produto = produtoService.findProdutoById(produtoId);
				
				if(!regra.getProdutos().contains(produto))
					throw new ViolacaoRegraNegocioException(String.format("Produto de código %d não vinculado a regra de código %d", produtoId, regraId));
				
				regra.removerProduto(produto);
				
			} catch(EntidadeNaoEncontradaException ex) {
				throw new ObjetoConflitanteException(String.format("Produto de código %d não cadastrado", produtoId), ex);
			}
		});
	}
	
	@Transactional
	public void excluir(Long promocaoId, Long regraId) {
		RegraPromocao regra = findRegraById(promocaoId, regraId);
		
		regraPromocaoRepository.delete(regra);
	}
	
	@Transactional
	public RegraPromocao findRegraById(Long promocaoId, Long regraId) {
		return regraPromocaoRepository.findByPromocaoIdAndId(promocaoId, regraId)
				 .orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Regra de código %d da promoção de código %d não encontrada", regraId, promocaoId)));
	}
	

	private void validarRegra(RegraPromocao regra) {
		if(TipoRegraPromocao.PRODUTO_GRATIS.equals(regra.getTipoRegra()) && regra.getProdutoGratis() != null && regra.getProdutoGratis().getId() != null) {
			try {
				Produto produto = produtoService.findProdutoById(regra.getProdutoGratis().getId());
				
				regra.setProdutoGratis(produto);
			} catch(EntidadeNaoEncontradaException e) {
				throw new ViolacaoRegraNegocioException(String.format("Produto de código %d não encontrado, para cadastro de produto grátis favor vincular a regra produtos cadastrados", regra.getProdutoGratis().getId()));
			}
		} else if (TipoRegraPromocao.PRODUTO_GRATIS.equals(regra.getTipoRegra())){
			throw new ViolacaoRegraNegocioException(String.format("Para regras do tipo %s deve ser informado o produto grátis", regra.getTipoRegra().getDescricao()));
		} else if (!TipoRegraPromocao.PRODUTO_GRATIS.equals(regra.getTipoRegra())  && regra.getProdutoGratis() != null && regra.getProdutoGratis().getId() != null){
			throw new ViolacaoRegraNegocioException(String.format("Para regras do tipo %s não pode ser informado o produto grátis", regra.getTipoRegra().getDescricao()));
		}
		
		if(isValorNuloOuZero(regra.getMeta()) || isValorNuloOuZero(regra.getValorRegra())) {
			throw new ViolacaoRegraNegocioException("As informações de valor e meta da regra é obrigatório");
		}
	}
	
	private boolean isValorNuloOuZero(BigDecimal valor) {
		return valor == null || valor.compareTo(BigDecimal.ZERO) == 0;
	}
}
