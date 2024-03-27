package com.pub.domain.service;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pub.domain.exception.EntidadeNaoEncontradaException;
import com.pub.domain.exception.ObjetoConflitanteException;
import com.pub.domain.exception.ViolacaoRegraNegocioException;
import com.pub.domain.model.Lancamento;
import com.pub.domain.model.enums.ModalidadeLancamento;
import com.pub.domain.repository.LancamentoRepository;
import com.pub.domain.service.dto.LancamentoFiltroDTO;
import com.pub.infrastructure.repository.spec.LancamentoSpecs;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LancamentoService {

	private final LancamentoRepository lancamentoRepository;
	
	
	@Transactional
	public Page<Lancamento> pesquisarLancamentos(LancamentoFiltroDTO lancamentoDTO, Pageable pageable) {
		
		Specification<Lancamento> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
		
		if(lancamentoDTO.getDataInicio() != null) {
			spec = spec.and(LancamentoSpecs.comDataMaiorOuIgualA(lancamentoDTO.getDataInicio()));
		}
		
		if(lancamentoDTO.getDataFim() != null) {
			spec = spec.and(LancamentoSpecs.comDataMenorOuIgualA(lancamentoDTO.getDataFim()));
		}
		
		if(lancamentoDTO.getLancamentoId() != null) {
			spec = spec.and(LancamentoSpecs.comIdIgualA(lancamentoDTO.getLancamentoId()));
		}
		
		if(lancamentoDTO.getModalidade() != null) {
			spec = spec.and(LancamentoSpecs.comModalidadeIgualA(lancamentoDTO.getModalidade()));
		}
		
		if(lancamentoDTO.getDescricao() != null) {
			spec = spec.and(LancamentoSpecs.comDescricaoParecida(lancamentoDTO.getDescricao()));
		}
		
		if(lancamentoDTO.getTipoLancamento() != null) {
			spec = spec.and(LancamentoSpecs.comTipoIgualA(lancamentoDTO.getTipoLancamento()));
		}
		
		if(lancamentoDTO.getProdutoId() != null) {
			spec = spec.and(LancamentoSpecs.comProdutoIdIgualA(lancamentoDTO.getProdutoId()));
		}
		
		return lancamentoRepository.findAll(spec, pageable);
	}
	
	@Transactional
	public Lancamento cadastrarLancamento(Lancamento lancamento, boolean validarModalidade) {
		
		if(validarModalidade) {
			validarModalidade(lancamento.getModalidade());
		}
		
		return this.lancamentoRepository.save(lancamento);
	}
	

	@Transactional
	public Lancamento atualizarLancamento(Lancamento lancamento, Long lancamentoId, boolean validarModalidade) {
		Lancamento lancamentoCadastrado = findLancamentoPorId(lancamentoId);
		
		if(lancamentoCadastrado.getModalidade().equals(ModalidadeLancamento.ALTERACAO_ESTOQUE)
				|| lancamentoCadastrado.getModalidade().equals(ModalidadeLancamento.ATENDIMENTO)) {
			throw new ViolacaoRegraNegocioException(String.format("Lançamento de modalidade %s, não pode ser alterado pois é uma modalidade de lançamento automático", lancamentoCadastrado.getModalidade().getDescricao()));
		}
		
		if(validarModalidade) {
			validarModalidade(lancamento.getModalidade());
		}
		
		BeanUtils.copyProperties(lancamento, lancamentoCadastrado, "id", "historicoProduto", "dataCadastro");
		
		return lancamentoCadastrado;
	}
	
	private void validarModalidade(ModalidadeLancamento modalidade) {
		if(ModalidadeLancamento.ALTERACAO_ESTOQUE.equals(modalidade) || ModalidadeLancamento.ATENDIMENTO.equals(modalidade))
			throw new ViolacaoRegraNegocioException(String.format("Lançamento não pode ser da modalidade %s, pois é uma modalidade de lançamento automático", modalidade.getDescricao()));
	}
	
	@Transactional
	public void excluirLancamento(Long lancamentoId) {
		try {
			Lancamento lancamento = findLancamentoPorId(lancamentoId);
			
			if(lancamento.getModalidade().equals(ModalidadeLancamento.ALTERACAO_ESTOQUE)
					|| lancamento.getModalidade().equals(ModalidadeLancamento.ATENDIMENTO)) {
				throw new ViolacaoRegraNegocioException(String.format("Lançamento de modalidade %s, não pode ser excluído pois é uma modalidade de lançamento automático", lancamento.getModalidade().getDescricao()));
			}
			
			lancamentoRepository.delete(lancamento);
			
			lancamentoRepository.flush();
			
		} catch(DataIntegrityViolationException ex) {
			throw new ObjetoConflitanteException(String.format("Lançamento de código %d está vinculado a uma alteração de estoque de um produto, portanto não pode ser excluída", lancamentoId));
		}
	}
	
	
	@Transactional
	public Lancamento findLancamentoPorId(Long lancamentoId) {
		return lancamentoRepository.findById(lancamentoId)
				 .orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Lancamento de código %d não encontrada", lancamentoId)));
	}
}
