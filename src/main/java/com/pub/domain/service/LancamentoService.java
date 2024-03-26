package com.pub.domain.service;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pub.domain.exception.EntidadeNaoEncontradaException;
import com.pub.domain.model.Lancamento;
import com.pub.domain.repository.LancamentoRepository;
import com.pub.domain.service.dto.LancamentoDTO;
import com.pub.infrastructure.repository.spec.LancamentoSpecs;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LancamentoService {

	private final LancamentoRepository lancamentoRepository;
	
	
	@Transactional
	public Page<Lancamento> pesquisarLancamentos(LancamentoDTO lancamentoDTO, Pageable pageable) {
		
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
			spec = spec.and(LancamentoSpecs.comModalidadeIgualA(lancamentoDTO.getModalidade().getDescricao()));
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
	public Lancamento cadastrarLancamento(Lancamento lancamento) {
		return this.lancamentoRepository.save(lancamento);
	}
	
	@Transactional
	public Lancamento atualizarLancamento(Lancamento lancamento, Long lancamentoId) {
		Lancamento lancamentoCadastrado = findLancamentoPorId(lancamentoId);
		
		BeanUtils.copyProperties(lancamento, lancamentoCadastrado, "id", "historicoProduto");
		
		return lancamentoCadastrado;
	}
	
	
	@Transactional
	public Lancamento findLancamentoPorId(Long lancamentoId) {
		return lancamentoRepository.findById(lancamentoId)
				 .orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Lancamento de código %d não encontrada", lancamentoId)));
	}
}
