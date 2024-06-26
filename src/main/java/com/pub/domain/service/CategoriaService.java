package com.pub.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import com.pub.domain.model.Categoria;
import com.pub.domain.model.UnidadeConversao;
import com.pub.domain.repository.CategoriaRepository;
import com.pub.infrastructure.repository.spec.CategoriaSpecs;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoriaService {

	private final CategoriaRepository categoriaRepository;
	
	private final UnidadeConversaoService unidadeConversaoService;
	
	@Transactional
	public Categoria cadastrarCategoria(Categoria categoria) {
		Optional<Categoria> categoriaSalvaOpt = categoriaRepository.findByNomeIgnoreCase(categoria.getNome());
		
		if(categoriaSalvaOpt.isPresent()) {
			throw new ObjetoJaCadastradoException(String.format("Já existe uma categoria de nome %s cadastrada", categoria.getNome()));
		}
	
		return this.categoriaRepository.save(categoria);
	}
	
	@Transactional
	public Categoria atualizarCategoria(Categoria categoria, Long categoriaId) {
		Categoria categoriaSalva = findCategoriaById(categoriaId);
		
		Optional<Categoria> categoriaSalvaOpt = categoriaRepository.findByNomeIgnoreCase(categoria.getNome());
		
		if(categoriaSalvaOpt.isPresent() && (categoriaSalvaOpt.get().getId() != categoriaId)) {
			throw new ObjetoJaCadastradoException(String.format("Já existe uma categoria de nome %s cadastrada", categoria.getNome()));
		}
		
		BeanUtils.copyProperties(categoria, categoriaSalva, "id", "unidadesConversao");
	
		return categoriaSalva;
	}
	
	public Page<Categoria> listarCategorias(String nome, Pageable pageable) {
		
		Specification<Categoria> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
		
		if(nome != null) {
			spec = spec.and(CategoriaSpecs.comNomeParecido(nome));
		}
		
		return categoriaRepository.findAll(spec, pageable);
	}
	
	@Transactional
	public boolean existsCategoriaById(Long categoriaId) {
		boolean exists = categoriaRepository.existsById(categoriaId);
		
		if(!exists) 
			throw new ViolacaoRegraNegocioException(String.format("Categoria de código %d não cadastrada", categoriaId));
		
		return true;
	}
	
	@Transactional
	public Set<UnidadeConversao> listarConversoesCategoria(Long categoriaId) {
		Categoria categoria = findCategoriaById(categoriaId);
		
		return categoria.getUnidadesConversao();
	}
	
	@Transactional
	public void associarConversoes(Long categoriaId, List<Long> conversoesIds) {
		Categoria categoria = findCategoriaById(categoriaId);
		
		conversoesIds.forEach(conversao -> {
			try {
				
				UnidadeConversao unidadeConversao = unidadeConversaoService.findUnidadeConversaoById(conversao);
				
				categoria.adicionarUnidadeConversao(unidadeConversao);
				
			} catch(EntidadeNaoEncontradaException ex) {
				throw new ObjetoConflitanteException(String.format("Conversão de id %d não cadastrada", conversao), ex);
			}
		});
	}
	
	@Transactional
	public void desassociarConversoes(Long categoriaId, List<Long> conversoesIds) {
		Categoria categoria = findCategoriaById(categoriaId);
		
		conversoesIds.forEach(conversaoId -> {
			try {
				
				UnidadeConversao unidadeConversao = unidadeConversaoService.findUnidadeConversaoById(conversaoId);
				
				if(!categoria.getUnidadesConversao().contains(unidadeConversao))
					throw new ViolacaoRegraNegocioException(String.format("Conversão de id %d não vinculada a categoria %s", conversaoId, categoria.getNome()));
				
				categoria.removerUnidadeConversao(unidadeConversao);
				
			} catch(EntidadeNaoEncontradaException ex) {
				throw new ObjetoConflitanteException(String.format("Conversão de id %d não cadastrada", conversaoId), ex);
			}
		});
	}
	
	@Transactional
	public void excluirCategoria(Long categoriaId) {
		try {
			Categoria categoria = findCategoriaById(categoriaId);
			
			categoriaRepository.delete(categoria);
			
			categoriaRepository.flush();
			
		} catch(DataIntegrityViolationException ex) {
			throw new ObjetoConflitanteException(String.format("Categoria de código %d possui produtos vinculados, portanto não pode ser excluída", categoriaId));
		}
	}
	
	@Transactional
	public Categoria findCategoriaById(Long categoriaId) {
		return categoriaRepository.findById(categoriaId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Categoria de código %d não encontrada", categoriaId)));
	}
}
