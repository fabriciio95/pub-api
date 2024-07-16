package com.pub.domain.service;

import static com.pub.infrastructure.repository.spec.MesaSpecs.comCapacidadeIgualA;
import static com.pub.infrastructure.repository.spec.MesaSpecs.comMesaIdIgualA;
import static com.pub.infrastructure.repository.spec.MesaSpecs.comNumeroIgualA;
import static com.pub.infrastructure.repository.spec.MesaSpecs.comStatusIgualA;

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
import com.pub.domain.model.Mesa;
import com.pub.domain.model.enums.StatusMesa;
import com.pub.domain.repository.MesaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MesaService {

	private final MesaRepository mesaRepository;
	
	
	public Page<Mesa> pesquisar(Long mesaId, Integer numero, StatusMesa status, Integer capacidade, Pageable pageable) {
			
			Specification<Mesa> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
			
			if(mesaId != null) 
				spec = spec.and(comMesaIdIgualA(mesaId));
			
			if(numero != null)
				spec = spec.and(comNumeroIgualA(numero));
			
			if(status != null)
				spec = spec.and(comStatusIgualA(status));
			
			if(capacidade != null)
				spec = spec.and(comCapacidadeIgualA(capacidade));
			
			return this.mesaRepository.findAll(spec, pageable);
		}
	
	@Transactional
	public Mesa cadastrar(Mesa mesa) {
		if(mesaRepository.existsByNumero(mesa.getNumero())) {
			throw new ViolacaoRegraNegocioException(String.format("Já existe uma mesa cadastrada com o número %d", mesa.getNumero()));
		}
		
		mesa.setStatus(StatusMesa.LIVRE);
		
		return mesaRepository.save(mesa);
	}
	
	@Transactional
	public Mesa atualizar(Long mesaId, Mesa mesa) {
		Mesa mesaCadastrada = findMesaById(mesaId);
		
		if(mesaRepository.existsByNumeroAndIdNot(mesa.getNumero(), mesaId)) {
			throw new ViolacaoRegraNegocioException(String.format("Já existe outra mesa cadastrada com o número %d", mesa.getNumero()));
		}
		
		BeanUtils.copyProperties(mesa, mesaCadastrada, "id", "status");
		
		return mesaCadastrada;
	}
	
	@Transactional
	public void excluir(Long mesaId) {
		try {
			Mesa mesa = findMesaById(mesaId);
			
			mesaRepository.delete(mesa);
			
			mesaRepository.flush();
			
		} catch(DataIntegrityViolationException ex) {
			throw new ObjetoConflitanteException(String.format("Mesa de código %d possui registros vinculados, portanto não pode ser excluída", mesaId));
		}
	}
	

	@Transactional
	public Mesa findMesaById(Long mesaId) {
		return mesaRepository.findById(mesaId)
					.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Mesa de código %d não encontrada", mesaId)));
	}
	
}
