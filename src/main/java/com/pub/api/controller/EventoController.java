package com.pub.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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

import com.pub.api.dto.evento.EventoDTO;
import com.pub.api.dto.evento.EventoInputDTO;
import com.pub.api.exception.RequisicaoInvalidaException;
import com.pub.api.mapper.assembler.EventoAssembler;
import com.pub.api.mapper.assembler.PaginaAssembler;
import com.pub.api.mapper.disassembler.EventoDisassembler;
import com.pub.api.validation.ValidationGroups.AtualizacaoGroup;
import com.pub.domain.model.Evento;
import com.pub.domain.model.enums.StatusEvento;
import com.pub.domain.service.EventoService;
import com.pub.domain.service.dto.EventoFiltroDTO;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/eventos")
public class EventoController {
	
	private final EventoService eventoService;
	
	private final EventoAssembler eventoAssembler;
	
	private final EventoDisassembler eventoDisassembler;

	private final PaginaAssembler paginaAssembler;
	
	
    @GetMapping
    public Page<EventoDTO> pesquisar(
    		@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataInicioPeriodoInicio, 
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataInicioPeriodoFim,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataFimPeriodoInicio, 
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataFimPeriodoFim,
			@RequestParam(required = false) Long eventoId, 
			@RequestParam(required = false) String descricao, 
			@RequestParam(required = false) StatusEvento status,
			Pageable pageable
    		) {
    	
    	validarDatas(dataInicioPeriodoInicio, dataInicioPeriodoFim, dataFimPeriodoInicio, dataFimPeriodoFim);
    	
    	EventoFiltroDTO filtro = EventoFiltroDTO.builder()
    			                                     .dataInicioPeriodoInicio(dataInicioPeriodoInicio)
    			                                     .dataInicioPeriodoFim(dataInicioPeriodoFim)
    			                                     .dataFimPeriodoInicio(dataFimPeriodoInicio)
    			                                     .dataFimPeriodoFim(dataFimPeriodoFim)
    			                                     .id(eventoId)
    			                                     .descricao(descricao)
    			                                     .status(status)
    											.build();
    	
    	Page<Evento> page = eventoService.pesquisar(filtro, pageable);
    	
    	List<EventoDTO> payloadPage = eventoAssembler.toListDto(page.getContent());
    	
		return paginaAssembler.toPage(payloadPage, pageable, page.getTotalElements());
    }
    
    @GetMapping("/{eventoId}")
    public EventoDTO findEventoPorId(@PathVariable Long eventoId) {
    	return eventoAssembler.toDto(eventoService.findEventoById(eventoId));
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EventoDTO cadastrar(@RequestBody @Valid EventoInputDTO eventoInputDto) {
    	Evento evento = eventoDisassembler.toEntidade(eventoInputDto);
    	
    	evento = eventoService.cadastrar(evento);
    	
    	return eventoAssembler.toDto(evento);
    }
    
    @PutMapping("/{eventoId}")
    public EventoDTO atualizar(@RequestBody @Validated({ AtualizacaoGroup.class, Default.class }) EventoInputDTO eventoInputDto, @PathVariable Long eventoId) {
    	Evento evento = eventoDisassembler.toEntidade(eventoInputDto);
    	
    	evento = eventoService.atualizar(evento, eventoId);
    	
    	return eventoAssembler.toDto(evento);
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{eventoId}")
    public void excluirEvento(@PathVariable Long eventoId) {
    	eventoService.excluirEvento(eventoId);
    }
    
	private void validarDatas(LocalDate dataInicioPeriodoInicio, LocalDate dataInicioPeriodoFim,
			LocalDate dataFimPeriodoInicio, LocalDate dataFimPeriodoFim) {
		
		if((dataInicioPeriodoInicio != null && dataInicioPeriodoFim != null) && dataInicioPeriodoInicio.isAfter(dataInicioPeriodoFim))
			throw new RequisicaoInvalidaException("Data ínicio deve ser menor ou igual a data fim");
		
		
		if((dataFimPeriodoInicio != null && dataFimPeriodoFim != null) && dataFimPeriodoInicio.isAfter(dataFimPeriodoFim))
			throw new RequisicaoInvalidaException("Data ínicio deve ser menor ou igual a data fim");
	}
}
