package com.pub.api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pub.api.dto.unidade.UnidadeDTO;
import com.pub.api.dto.unidade.UnidadeInputDTO;
import com.pub.api.mapper.assembler.UnidadeAssembler;
import com.pub.api.mapper.assembler.PaginaAssembler;
import com.pub.api.mapper.disassembler.UnidadeDisassembler;
import com.pub.domain.model.Unidade;
import com.pub.domain.service.UnidadeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/unidades")
@RestController
public class UnidadeController {

	private final UnidadeService unidadeService;
	
	private final UnidadeAssembler unidadeAssembler;
	
	private final UnidadeDisassembler unidadeDisassembler;
	
	private final PaginaAssembler paginaAssembler;
	
	@GetMapping("/{unidadeId}")
	public UnidadeDTO findUnidadePorId(@PathVariable Long unidadeId) {
		return unidadeAssembler.toDto(unidadeService.findUnidadeById(unidadeId));
	}
	
	@GetMapping
	public Page<UnidadeDTO> listarUnidades(@RequestParam(required = false) String nome, Pageable pageable) {
		 Page<Unidade> unidades = unidadeService.listarUnidades(nome, pageable);
		 
		 List<UnidadeDTO> unidadesDto = unidadeAssembler.toListDto(unidades.getContent());
		 
		 return paginaAssembler.toPage(unidadesDto, pageable, unidades.getTotalElements());
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public UnidadeDTO cadastrarUnidade(@RequestBody @Valid UnidadeInputDTO unidadeInputDTO) {
		Unidade unidade = unidadeDisassembler.toEntidade(unidadeInputDTO);
		
		return unidadeAssembler.toDto(unidadeService.cadastrarUnidade(unidade));
	}
	
	@PutMapping("/{unidadeId}")
	public UnidadeDTO atualizarUnidade(@RequestBody @Valid UnidadeInputDTO unidadeInputDTO, @PathVariable Long unidadeId) {
		Unidade unidade = unidadeDisassembler.toEntidade(unidadeInputDTO);
		
		return unidadeAssembler.toDto(unidadeService.atualizarUnidade(unidade, unidadeId));
	}
}
