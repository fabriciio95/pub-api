package com.pub.domain.service;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.exception.EntidadeNaoEncontradaException;
import com.pub.domain.exception.ObjetoConflitanteException;
import com.pub.domain.exception.ViolacaoRegraNegocioException;
import com.pub.domain.model.Evento;
import com.pub.domain.model.Lancamento;
import com.pub.domain.model.enums.ModalidadeLancamento;
import com.pub.domain.repository.LancamentoRepository;
import com.pub.domain.service.dto.LancamentoFiltroDTO;
import com.pub.infrastructure.repository.spec.LancamentoSpecs;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LancamentoService {

	private final LancamentoRepository lancamentoRepository;
	
	private final EventoService eventoService;
	
	
	@Transactional
	public Page<Lancamento> pesquisarLancamentos(LancamentoFiltroDTO filtro, Pageable pageable) {
		
		Specification<Lancamento> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
		
		if(filtro.getDataInicio() != null) {
			spec = spec.and(LancamentoSpecs.comDataMaiorOuIgualA(filtro.getDataInicio()));
		}
		
		if(filtro.getDataFim() != null) {
			spec = spec.and(LancamentoSpecs.comDataMenorOuIgualA(filtro.getDataFim()));
		}
		
		if(filtro.getLancamentoId() != null) {
			spec = spec.and(LancamentoSpecs.comIdIgualA(filtro.getLancamentoId()));
		}
		
		if(filtro.getModalidade() != null) {
			spec = spec.and(LancamentoSpecs.comModalidadeIgualA(filtro.getModalidade()));
		}
		
		if(filtro.getDescricao() != null) {
			spec = spec.and(LancamentoSpecs.comDescricaoParecida(filtro.getDescricao()));
		}
		
		if(filtro.getTipoLancamento() != null) {
			spec = spec.and(LancamentoSpecs.comTipoIgualA(filtro.getTipoLancamento()));
		}
		
		if(filtro.getProdutoId() != null) {
			spec = spec.and(LancamentoSpecs.comProdutoIdIgualA(filtro.getProdutoId()));
		}
		
		if(filtro.getEventoId() != null) {
			spec = spec.and(LancamentoSpecs.comEventoIdIgualA(filtro.getEventoId()));
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
	
	@Transactional
	public void associacarEvento(Long lancamentoId, Long eventoId) {
		Lancamento lancamento = findLancamentoPorId(lancamentoId);
		
		Evento evento = eventoService.findEventoById(eventoId);
		
		lancamento.setEvento(evento);
	}
	
	@Transactional
	public void desassociacarEvento(Long lancamentoId, Long eventoId) {
		Lancamento lancamento = findLancamentoPorId(lancamentoId);
		
		if(eventoService.existsEventoById(eventoId)) {
			lancamento.setEvento(null);
			return;
		}
		
		throw new EntidadeNaoEncontradaException(String.format("Evento de código %d não encontrado", eventoId));
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
