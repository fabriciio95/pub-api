package com.pub.api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pub.api.dto.cliente.ClienteDTO;
import com.pub.api.dto.cliente.ClienteInputDTO;
import com.pub.api.mapper.assembler.ClienteAssembler;
import com.pub.api.mapper.assembler.PaginaAssembler;
import com.pub.api.mapper.disassembler.ClienteDisassembler;
import com.pub.domain.model.Cliente;
import com.pub.domain.service.ClienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/clientes")
@RequiredArgsConstructor
@RestController
public class ClienteController {

	private final ClienteService clienteService;
	
	private final ClienteDisassembler clienteDisassembler;
	
	private final ClienteAssembler clienteAssembler;
	
	private final PaginaAssembler paginaAssembler;

	@GetMapping
	public Page<ClienteDTO> buscarClientes(
			@RequestParam(required = false) Long clienteId,
			@RequestParam(required = false) String nome,
			@RequestParam(required = false) String telefone,
			@RequestParam(required = false) String cpf,
			Pageable pageable) {
		
		Page<Cliente> clientes = clienteService.pesquisar(clienteId, nome, telefone, cpf, pageable);
		
		List<ClienteDTO> contentPage = clienteAssembler.toListDTO(clientes.getContent());
		
		return paginaAssembler.toPage(contentPage, pageable, clientes.getTotalElements());
	}
	
	@GetMapping("/{clienteId}")
	public ClienteDTO findById(@PathVariable Long clienteId) {
		Cliente cliente = clienteService.findClienteById(clienteId);
		
		return clienteAssembler.toDTO(cliente);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ClienteDTO cadastrar(@RequestBody @Valid ClienteInputDTO clienteInputDto) {
		
		Cliente cliente = clienteDisassembler.toEntidade(clienteInputDto);
		
		cliente = clienteService.cadastrar(cliente);
		
		return clienteAssembler.toDTO(cliente);
	}
	
	@PutMapping("/{clienteId}")
	public ClienteDTO atualizar(@PathVariable Long clienteId, @RequestBody @Valid ClienteInputDTO clienteInputDTO) {
		
		Cliente cliente = clienteDisassembler.toEntidade(clienteInputDTO);
		
		cliente = clienteService.atualizar(clienteId, cliente);
		
		return clienteAssembler.toDTO(cliente);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{clienteId}")
	public void excluir(@PathVariable Long clienteId) {
		clienteService.excluir(clienteId);
	}
}
