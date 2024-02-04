package com.pub.domain.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.exception.EntidadeNaoEncontradaException;
import com.pub.domain.exception.ObjetoConflitanteException;
import com.pub.domain.exception.ObjetoJaCadastradoException;
import com.pub.domain.model.UnidadeConversao;
import com.pub.domain.repository.UnidadeConversaoRepository;
import com.pub.infrastructure.repository.spec.UnidadeConversaoSpecs;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UnidadeConversaoService {

	private final UnidadeConversaoRepository unidadeConversaoRepository;
	
	
	@Transactional
	public UnidadeConversao cadastrarUnidadeConversao(UnidadeConversao unidadeConversao) {
		Optional<UnidadeConversao> unidadeConversaoSalvaOpt = unidadeConversaoRepository.findByDescricaoOrigemIgnoreCase(unidadeConversao.getDescricaoOrigem());
		
		if(unidadeConversaoSalvaOpt.isPresent()) {
			throw new ObjetoJaCadastradoException(String.format("Já existe uma conversão com a descrição %s cadastrada", unidadeConversao.getDescricaoOrigem()));
		}
	
		return this.unidadeConversaoRepository.save(unidadeConversao);
	}
	
	@Transactional
	public UnidadeConversao atualizarUnidadeConversao(UnidadeConversao unidadeConversao, Long unidadeConversaoId) {
		UnidadeConversao unidadeConversaoSalva = findUnidadeConversaoById(unidadeConversaoId);
		
		Optional<UnidadeConversao> unidadeConversaoSalvaOpt = unidadeConversaoRepository.findByDescricaoOrigemIgnoreCase(unidadeConversao.getDescricaoOrigem());
		
		if(unidadeConversaoSalvaOpt.isPresent() && (unidadeConversaoSalvaOpt.get().getId() != unidadeConversaoId)) {
			throw new ObjetoJaCadastradoException(String.format("Já existe uma conversão com a descrição %s cadastrada", unidadeConversao.getDescricaoOrigem()));
		}
		
		BeanUtils.copyProperties(unidadeConversao, unidadeConversaoSalva, "id");
	
		return unidadeConversaoSalva;
	}
	
	public Page<UnidadeConversao> listarUnidadeConversao(String descricao, Integer fatorConversao, Pageable pageable) {
		
		Specification<UnidadeConversao> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
		
		if(descricao != null) {
			spec = spec.and(UnidadeConversaoSpecs.comDescricaoOrigemParecida(descricao));
		}
		
		if(fatorConversao != null) {
			spec = spec.and(UnidadeConversaoSpecs.comFatorConversaoIgualA(fatorConversao));
		}
		
		return unidadeConversaoRepository.findAll(spec, pageable);
	}
	
	@Transactional
	public void excluirUnidadeConversao(Long unidadeConversaoId) {
		try {
			UnidadeConversao unidadeConversao = findUnidadeConversaoById(unidadeConversaoId);
			
			unidadeConversaoRepository.delete(unidadeConversao);
			
			unidadeConversaoRepository.flush();
			
		} catch(DataIntegrityViolationException ex) {
			throw new ObjetoConflitanteException(String.format("Conversão de código %d possui categorias vinculadas, portanto não pode ser excluída", unidadeConversaoId));
		}
	}
	
	@Transactional
	public UnidadeConversao findUnidadeConversaoById(Long unidadeConversaoId) {
		return unidadeConversaoRepository.findById(unidadeConversaoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Conversão de código %d não encontrada", unidadeConversaoId)));
	}
}
