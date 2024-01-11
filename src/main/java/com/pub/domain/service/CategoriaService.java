package com.pub.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.exception.ViolacaoRegraNegocioException;
import com.pub.domain.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoriaService {

	private final CategoriaRepository categoriaRepository;
	
	@Transactional
	public boolean existsCategoriaById(Long categoriaId) {
		boolean exists = categoriaRepository.existsById(categoriaId);
		
		if(!exists) 
			throw new ViolacaoRegraNegocioException(String.format("Categoria de código %d não cadastrada", categoriaId));
		
		return true;
	}
}
