package com.pub.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.exception.EntidadeNaoEncontradaException;
import com.pub.domain.model.UnidadeConversao;
import com.pub.domain.repository.UnidadeConversaoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UnidadeConversaoService {

	private final UnidadeConversaoRepository unidadeConversaoRepository;
	
	
	@Transactional
	public UnidadeConversao findUnidadeConversaoById(Long unidadeConversaoId) {
		return unidadeConversaoRepository.findById(unidadeConversaoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Conversão de código %d não encontrada", unidadeConversaoId)));
	}
}
