package com.pub.domain.service;

import static com.pub.infrastructure.repository.spec.ClienteSpecs.comClienteIdIgualA;
import static com.pub.infrastructure.repository.spec.ClienteSpecs.comCpfParecido;
import static com.pub.infrastructure.repository.spec.ClienteSpecs.comNomeParecido;
import static com.pub.infrastructure.repository.spec.ClienteSpecs.comTelefoneParecido;

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
import com.pub.domain.model.Cliente;
import com.pub.domain.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

	private final ClienteRepository clienteRepository;
	
	
	public Page<Cliente> pesquisar(Long clienteId, String nome, String telefone, String cpf, Pageable pageable) {
			
			Specification<Cliente> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
			
			if(clienteId != null) 
				spec = spec.and(comClienteIdIgualA(clienteId));
			
			if(nome != null)
				spec = spec.and(comNomeParecido(nome));
			
			if(telefone != null)
				spec = spec.and(comTelefoneParecido(telefone));
			
			if(cpf != null)
				spec = spec.and(comCpfParecido(cpf));
			
			return this.clienteRepository.findAll(spec, pageable);
		}
	
	@Transactional
	public Cliente cadastrar(Cliente cliente) {
		if(clienteRepository.existsByCpf(cliente.getCpf())) {
			throw new ViolacaoRegraNegocioException(String.format("Já existe um cliente cadastrada com o cpf %s", cliente.getCpf()));
		}
		
		if(clienteRepository.existsByTelefone(cliente.getTelefone())) {
			throw new ViolacaoRegraNegocioException(String.format("Já existe um cliente cadastrada com o telefone %s", cliente.getTelefone()));
		}
		
		return clienteRepository.save(cliente);
	}
	
	@Transactional
	public Cliente atualizar(Long clienteId, Cliente cliente) {
		Cliente clienteCadastrado = findClienteById(clienteId);
		
		if(clienteRepository.existsByCpfAndIdNot(cliente.getCpf(), clienteId)) {
			throw new ViolacaoRegraNegocioException(String.format("Já existe um outro cliente cadastrada com o cpf %s", cliente.getCpf()));
		}
		
		if(clienteRepository.existsByTelefoneAndIdNot(cliente.getTelefone(), clienteId)) {
			throw new ViolacaoRegraNegocioException(String.format("Já existe um outro cliente cadastrada com o telefone %s", cliente.getTelefone()));
		}
		
		BeanUtils.copyProperties(cliente, clienteCadastrado, "id");
		
		return clienteCadastrado;
	}
	
	@Transactional
	public void excluir(Long clienteId) {
		try {
			Cliente cliente = findClienteById(clienteId);
			
			clienteRepository.delete(cliente);
			
			clienteRepository.flush();
			
		} catch(DataIntegrityViolationException ex) {
			throw new ObjetoConflitanteException(String.format("Cliente de código %d possui registros vinculados, portanto não pode ser excluído", clienteId));
		}
	}
	

	@Transactional
	public Cliente findClienteById(Long clienteId) {
		return clienteRepository.findById(clienteId)
					.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Cliente de código %d não encontrado", clienteId)));
	}
	
}
