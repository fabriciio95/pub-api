package com.pub.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.model.PerdaAvaria;
import com.pub.domain.repository.PerdaAvariaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PerdaAvariaService {

	private final PerdaAvariaRepository perdaAvariaRepository;
	
	@Transactional
	public PerdaAvaria salvarPerdaAvaria(PerdaAvaria perdaAvaria) {
		return perdaAvariaRepository.save(perdaAvaria);
	}
}
