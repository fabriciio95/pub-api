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

import com.pub.api.dto.funcionario.FuncionarioDTO;
import com.pub.api.dto.funcionario.FuncionarioInputDTO;
import com.pub.api.mapper.assembler.FuncionarioAssembler;
import com.pub.api.mapper.assembler.PaginaAssembler;
import com.pub.api.mapper.disassembler.FuncionarioDisassembler;
import com.pub.domain.model.Funcionario;
import com.pub.domain.model.enums.FuncaoFuncionario;
import com.pub.domain.model.enums.StatusFuncionario;
import com.pub.domain.service.FuncionarioService;
import com.pub.domain.service.dto.FuncionarioFiltroDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/funcionarios")
@RequiredArgsConstructor
@RestController
public class FuncionarioController {

	private final FuncionarioService funcionarioService;
	
	private final FuncionarioDisassembler funcionarioDisassembler;
	
	private final FuncionarioAssembler funcionarioAssembler;
	
	private final PaginaAssembler paginaAssembler;

	@GetMapping
	public Page<FuncionarioDTO> buscarFuncionarios(
			@RequestParam(required = false) Long funcionarioId,
			@RequestParam(required = false) String nome,
			@RequestParam(required = false) String telefone,
			@RequestParam(required = false) String cpf,
			@RequestParam(required = false) FuncaoFuncionario funcao,
			@RequestParam(required = false) StatusFuncionario status,
			Pageable pageable) {
		
		FuncionarioFiltroDTO filtro = FuncionarioFiltroDTO.builder()
															.funcionarioId(funcionarioId)
															.nome(nome)
															.telefone(telefone)
															.cpf(cpf)
															.funcao(funcao)
															.status(status)
														  .build();
		
		
		Page<Funcionario> funcionarios = funcionarioService.pesquisar(filtro, pageable);
		
		List<FuncionarioDTO> contentPage = funcionarioAssembler.toListDTO(funcionarios.getContent());
		
		return paginaAssembler.toPage(contentPage, pageable, funcionarios.getTotalElements());
	}
	
	@GetMapping("/{funcionarioId}")
	public FuncionarioDTO findById(@PathVariable Long funcionarioId) {
		Funcionario funcionario = funcionarioService.findFuncionarioById(funcionarioId);
		
		return funcionarioAssembler.toDTO(funcionario);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public FuncionarioDTO cadastrar(@RequestBody @Valid FuncionarioInputDTO funcionarioInputDto) {
		
		Funcionario funcionario = funcionarioDisassembler.toEntidade(funcionarioInputDto);
		
		funcionario = funcionarioService.cadastrar(funcionario);
		
		return funcionarioAssembler.toDTO(funcionario);
	}
	
	@PutMapping("/{funcionarioId}")
	public FuncionarioDTO atualizar(@PathVariable Long funcionarioId, @RequestBody @Valid FuncionarioInputDTO funcionarioInputDTO) {
		
		Funcionario funcionario = funcionarioDisassembler.toEntidade(funcionarioInputDTO);
		
		funcionario = funcionarioService.atualizar(funcionarioId, funcionario);
		
		return funcionarioAssembler.toDTO(funcionario);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{funcionarioId}")
	public void excluir(@PathVariable Long funcionarioId) {
		funcionarioService.excluir(funcionarioId);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/{funcionarioId}/ativacao")
	public void ativarRegra(@PathVariable Long funcionarioId) {
		funcionarioService.alterarStatus(funcionarioId, StatusFuncionario.ATIVO);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{funcionarioId}/ativacao")
	public void desativarRegra(@PathVariable Long funcionarioId) {
		funcionarioService.alterarStatus(funcionarioId, StatusFuncionario.INATIVO);
	}
}
