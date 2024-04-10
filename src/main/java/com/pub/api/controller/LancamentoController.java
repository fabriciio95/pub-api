package com.pub.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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

import com.pub.api.dto.lancamento.LancamentoDTO;
import com.pub.api.dto.lancamento.LancamentoInputDTO;
import com.pub.api.exception.RequisicaoInvalidaException;
import com.pub.api.mapper.assembler.LancamentoAssembler;
import com.pub.api.mapper.assembler.PaginaAssembler;
import com.pub.api.mapper.disassembler.LancamentoDisassembler;
import com.pub.domain.model.Lancamento;
import com.pub.domain.model.enums.ModalidadeLancamento;
import com.pub.domain.model.enums.TipoLancamento;
import com.pub.domain.service.LancamentoService;
import com.pub.domain.service.dto.LancamentoFiltroDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/lancamentos")
@RestController
public class LancamentoController {

	private final LancamentoService lancamentoService;
	
	private final LancamentoAssembler lancamentoAssembler;
	
	private final LancamentoDisassembler lancamentoDisassembler;
	
	private final PaginaAssembler paginaAssembler;
	
	@GetMapping
	public Page<LancamentoDTO> pesquisarLancamentos(Pageable pageable, 
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataInicio, 
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataFim,
			@RequestParam(required = false) Long produtoId, 
			@RequestParam(required = false) Long eventoId, 
			@RequestParam(required = false) Long lancamentoId, 
			@RequestParam(required = false) String modalidade, 
			@RequestParam(required = false) String descricao, 
			@RequestParam(required = false) TipoLancamento tipoLancamento) {
		
		if((dataInicio != null && dataFim != null) && dataInicio.isAfter(dataFim))
			throw new RequisicaoInvalidaException("Data ínicio deve ser menor ou igual a data fim");
				
		LancamentoFiltroDTO lancamentoFiltroDTO = LancamentoFiltroDTO.builder()
																		.dataInicio(dataInicio)
																		.dataFim(dataFim)
																		.produtoId(produtoId)
																		.eventoId(eventoId)
																		.lancamentoId(lancamentoId)
																		.modalidade(ModalidadeLancamento.findModalidadePorDescricao(modalidade, false))
																		.descricao(descricao)
																		.tipoLancamento(tipoLancamento)
				                                                     .build();
		
		Page<Lancamento> lacamentos = lancamentoService.pesquisarLancamentos(lancamentoFiltroDTO, pageable);
		
		List<LancamentoDTO> conteudoPagina = lancamentoAssembler.toListDto(lacamentos.getContent());
		
		return paginaAssembler.toPage(conteudoPagina, pageable, lacamentos.getTotalElements());
	}
	
	@GetMapping("/{lancamentoId}")
	public LancamentoDTO findLancamentoPorId(@PathVariable Long lancamentoId) {
		Lancamento lancamento = lancamentoService.findLancamentoPorId(lancamentoId);
		
		return lancamentoAssembler.toDto(lancamento);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LancamentoDTO cadastrarLancamento(@RequestBody @Valid LancamentoInputDTO lancamentoInputDTO) {
		Lancamento lancamento = lancamentoDisassembler.toEntidade(lancamentoInputDTO);
		
		lancamento = lancamentoService.cadastrarLancamento(lancamento, true);
		
		return lancamentoAssembler.toDto(lancamento);
	}
	
	@PutMapping("/{lancamentoId}")
	public LancamentoDTO atualizarLancamento(@RequestBody @Valid LancamentoInputDTO lancamentoInputDTO, @PathVariable Long lancamentoId) {
		Lancamento lancamento = lancamentoDisassembler.toEntidade(lancamentoInputDTO);
		
		lancamento = lancamentoService.atualizarLancamento(lancamento, lancamentoId, true);
		
		return lancamentoAssembler.toDto(lancamento);
	}
	
	@DeleteMapping("/{lancamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluirLancamento(@PathVariable Long lancamentoId) {
		lancamentoService.excluirLancamento(lancamentoId);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/{lancamentoId}/associacao-evento/{eventoId}")
	public void associarEvento(@PathVariable Long lancamentoId, @PathVariable Long eventoId) {
		lancamentoService.associacarEvento(lancamentoId, eventoId);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{lancamentoId}/associacao-evento/{eventoId}")
	public void desassociarEvento(@PathVariable Long lancamentoId, @PathVariable Long eventoId) {
		lancamentoService.desassociacarEvento(lancamentoId, eventoId);
	}
}
