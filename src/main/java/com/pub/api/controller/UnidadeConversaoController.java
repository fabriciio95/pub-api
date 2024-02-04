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

import com.pub.api.dto.unidadeConversao.UnidadeConversaoDTO;
import com.pub.api.dto.unidadeConversao.UnidadeConversaoInputDTO;
import com.pub.api.mapper.assembler.PaginaAssembler;
import com.pub.api.mapper.assembler.UnidadeConversaoAssembler;
import com.pub.api.mapper.disassembler.UnidadeConversaoDisassembler;
import com.pub.domain.model.UnidadeConversao;
import com.pub.domain.service.UnidadeConversaoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/conversoes")
@RestController
public class UnidadeConversaoController {

	private final UnidadeConversaoService unidadeConversaoService;
	
	private final UnidadeConversaoAssembler unidadeConversaoAssembler;
	
	private final UnidadeConversaoDisassembler unidadeConversaoDisassembler;
	
	private final PaginaAssembler paginaAssembler;
	
	@GetMapping
	public Page<UnidadeConversaoDTO> pesquisarUnidadeConversao(@RequestParam(required = false) String descricao, 
			@RequestParam(required = false) Integer fatorConversao, Pageable pageable) {
		Page<UnidadeConversao> conversoes = unidadeConversaoService.listarUnidadeConversao(descricao, fatorConversao, pageable);
		
		List<UnidadeConversaoDTO> conversoesDTO = unidadeConversaoAssembler.toListDto(conversoes.getContent());
		
		return paginaAssembler.toPage(conversoesDTO, pageable, conversoes.getTotalElements());
	}
	
	@GetMapping("/{unidadeConversaoId}")
	public UnidadeConversaoDTO findUnidadeConversaoPorId(@PathVariable Long unidadeConversaoId) {
		return unidadeConversaoAssembler.toDto(unidadeConversaoService.findUnidadeConversaoById(unidadeConversaoId));
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public UnidadeConversaoDTO cadastrarUnidadeConversao(@RequestBody @Valid UnidadeConversaoInputDTO unidadeConversaoInputDTO) {
		UnidadeConversao unidadeConversao = unidadeConversaoDisassembler.toEntidade(unidadeConversaoInputDTO);
		
		return unidadeConversaoAssembler.toDto(unidadeConversaoService.cadastrarUnidadeConversao(unidadeConversao));
	}
	
	@PutMapping("/{unidadeConversaoId}")
	public UnidadeConversaoDTO atualizarUnidadeConversao(@RequestBody @Valid UnidadeConversaoInputDTO unidadeConversaoInputDTO, @PathVariable Long unidadeConversaoId) {
		UnidadeConversao unidadeConversao = unidadeConversaoDisassembler.toEntidade(unidadeConversaoInputDTO);
		
		return unidadeConversaoAssembler.toDto(unidadeConversaoService.atualizarUnidadeConversao(unidadeConversao, unidadeConversaoId));
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{unidadeConversaoId}")
	public void excluirUnidadeConversao(@PathVariable Long unidadeConversaoId) {
		unidadeConversaoService.excluirUnidadeConversao(unidadeConversaoId);
	}
}
