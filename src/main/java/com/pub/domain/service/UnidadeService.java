package com.pub.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.exception.ViolacaoRegraNegocioException;
import com.pub.domain.repository.UnidadeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UnidadeService {

	private final UnidadeRepository unidadeRepository;
	
	@Transactional
	public boolean existsUnidadeById(Long unidadeId) {
		boolean exists = unidadeRepository.existsById(unidadeId);
		
		if(!exists) 
			throw new ViolacaoRegraNegocioException(String.format("Unidade de código %d não cadastrada", unidadeId));
		
		return true;
	}
}
