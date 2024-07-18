package com.pub.domain.service;

import static com.pub.infrastructure.repository.spec.FuncionarioSpecs.*;

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
import com.pub.domain.model.Funcionario;
import com.pub.domain.model.enums.StatusFuncionario;
import com.pub.domain.repository.FuncionarioRepository;
import com.pub.domain.service.dto.FuncionarioFiltroDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

	private final FuncionarioRepository funcionarioRepository;
	
	
	public Page<Funcionario> pesquisar(FuncionarioFiltroDTO filtro, Pageable pageable) {
			
			Specification<Funcionario> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
			
			if(filtro.getFuncionarioId() != null) 
				spec = spec.and(comFuncionarioIdIgualA(filtro.getFuncionarioId()));
			
			if(filtro.getNome() != null)
				spec = spec.and(comNomeParecido(filtro.getNome()));
			
			if(filtro.getTelefone() != null)
				spec = spec.and(comTelefoneParecido(filtro.getTelefone()));
			
			if(filtro.getCpf() != null)
				spec = spec.and(comCpfParecido(filtro.getCpf()));
			
			if(filtro.getStatus() != null) 
				spec = spec.and(comStatusIgualA(filtro.getStatus()));
			
			if(filtro.getFuncao() != null)
				spec = spec.and(comFuncaoIgualA(filtro.getFuncao()));
			
			return this.funcionarioRepository.findAll(spec, pageable);
		}
	
	@Transactional
	public Funcionario cadastrar(Funcionario funcionario) {
		if(funcionarioRepository.existsByCpf(funcionario.getCpf())) {
			throw new ViolacaoRegraNegocioException(String.format("Já existe um funcionario cadastrada com o cpf %s", funcionario.getCpf()));
		}
		
		if(funcionarioRepository.existsByTelefone(funcionario.getTelefone())) {
			throw new ViolacaoRegraNegocioException(String.format("Já existe um funcionario cadastrada com o telefone %s", funcionario.getTelefone()));
		}
		
		
		funcionario.setStatus(StatusFuncionario.ATIVO);
		
		return funcionarioRepository.save(funcionario);
	}
	
	@Transactional
	public Funcionario atualizar(Long funcionarioId, Funcionario funcionario) {
		Funcionario funcionarioCadastrado = findFuncionarioById(funcionarioId);
		
		if(funcionarioRepository.existsByCpfAndIdNot(funcionario.getCpf(), funcionarioId)) {
			throw new ViolacaoRegraNegocioException(String.format("Já existe um outro funcionario cadastrada com o cpf %s", funcionario.getCpf()));
		}
		
		if(funcionarioRepository.existsByTelefoneAndIdNot(funcionario.getTelefone(), funcionarioId)) {
			throw new ViolacaoRegraNegocioException(String.format("Já existe um outro funcionario cadastrada com o telefone %s", funcionario.getTelefone()));
		}
		
		BeanUtils.copyProperties(funcionario, funcionarioCadastrado, "id", "status");
		
		return funcionarioCadastrado;
	}
	
	
	@Transactional
	public void alterarStatus(Long funcionarioId, StatusFuncionario status) {
		Funcionario funcionario = findFuncionarioById(funcionarioId);
		
		funcionario.setStatus(status);
	}
	
	@Transactional
	public void excluir(Long funcionarioId) {
		try {
			Funcionario funcionario = findFuncionarioById(funcionarioId);
			
			funcionarioRepository.delete(funcionario);
			
			funcionarioRepository.flush();
			
		} catch(DataIntegrityViolationException ex) {
			throw new ObjetoConflitanteException(String.format("Funcionario de código %d possui registros vinculados, portanto não pode ser excluído", funcionarioId));
		}
	}
	

	@Transactional
	public Funcionario findFuncionarioById(Long funcionarioId) {
		return funcionarioRepository.findById(funcionarioId)
					.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Funcionario de código %d não encontrado", funcionarioId)));
	}
	
}
