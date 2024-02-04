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
import com.pub.domain.exception.ViolacaoRegraNegocioException;
import com.pub.domain.model.Unidade;
import com.pub.domain.repository.UnidadeRepository;
import com.pub.infrastructure.repository.spec.UnidadeSpecs;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UnidadeService {

	private final UnidadeRepository unidadeRepository;
	
	@Transactional
	public Unidade cadastrarUnidade(Unidade unidade) {
		Optional<Unidade> unidadeSalvaOpt = unidadeRepository.findByNomeIgnoreCase(unidade.getNome());
		
		if(unidadeSalvaOpt.isPresent()) {
			throw new ObjetoJaCadastradoException(String.format("Já existe uma unidade de nome %s cadastrada", unidade.getNome()));
		}
	
		return this.unidadeRepository.save(unidade);
	}
	
	@Transactional
	public Unidade atualizarUnidade(Unidade unidade, Long unidadeId) {
		Unidade unidadeSalva = findUnidadeById(unidadeId);
		
		Optional<Unidade> unidadeSalvaOpt = unidadeRepository.findByNomeIgnoreCase(unidade.getNome());
		
		if(unidadeSalvaOpt.isPresent() && (unidadeSalvaOpt.get().getId() != unidadeId)) {
			throw new ObjetoJaCadastradoException(String.format("Já existe uma unidade de nome %s cadastrada", unidade.getNome()));
		}
		
		BeanUtils.copyProperties(unidade, unidadeSalva, "id");
	
		return unidadeSalva;
	}
	
	public Page<Unidade> listarUnidades(String nome, Pageable pageable) {
		
		Specification<Unidade> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
		
		if(nome != null) {
			spec = spec.and(UnidadeSpecs.comNomeParecido(nome));
		}
		
		return unidadeRepository.findAll(spec, pageable);
	}
	
	@Transactional
	public boolean existsUnidadeById(Long unidadeId) {
		boolean exists = unidadeRepository.existsById(unidadeId);
		
		if(!exists) 
			throw new ViolacaoRegraNegocioException(String.format("Unidade de código %d não cadastrada", unidadeId));
		
		return true;
	}
	
	@Transactional
	public void excluirUnidade(Long unidadeId) {
		try {
			Unidade unidade = findUnidadeById(unidadeId);
			
			unidadeRepository.delete(unidade);
			
			unidadeRepository.flush();
			
		} catch(DataIntegrityViolationException ex) {
			throw new ObjetoConflitanteException(String.format("Unidade de código %d possui produtos vinculados, portanto não pode ser excluída", unidadeId));
		}
	}
	
	@Transactional
	public Unidade findUnidadeById(Long unidadeId) {
		return unidadeRepository.findById(unidadeId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Unidade de código %d não encontrada", unidadeId)));
	}
}
