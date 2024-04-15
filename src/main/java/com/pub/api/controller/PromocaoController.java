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

import com.pub.api.dto.promocao.PromocaoDTO;
import com.pub.api.dto.promocao.PromocaoInputDTO;
import com.pub.api.exception.RequisicaoInvalidaException;
import com.pub.api.mapper.assembler.PaginaAssembler;
import com.pub.api.mapper.assembler.PromocaoAssembler;
import com.pub.api.mapper.disassembler.PromocaoDisassembler;
import com.pub.domain.model.Promocao;
import com.pub.domain.model.enums.StatusPromocao;
import com.pub.domain.service.PromocaoService;
import com.pub.domain.service.dto.PromocaoFiltroDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/promocoes")
public class PromocaoController {
	
	private final PromocaoService promocaoService;
	
	private final PromocaoAssembler promocaoAssembler;
	
	private final PromocaoDisassembler promocaoDisassembler;

	private final PaginaAssembler paginaAssembler;
	
	
    @GetMapping
    public Page<PromocaoDTO> pesquisar(
    		@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataInicio, 
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataFim,
			@RequestParam(required = false) Long promocaoId, 
			@RequestParam(required = false) String descricao, 
			@RequestParam(required = false) StatusPromocao status,
			Pageable pageable
    		) {
    	
    	validarDatas(dataInicio, dataFim);
    	
    	PromocaoFiltroDTO filtro = PromocaoFiltroDTO.builder()
    			                                     .dataInicio(dataInicio)
    			                                     .dataFim(dataFim)
    			                                     .descricao(descricao)
    			                                     .id(promocaoId)
    			                                     .status(status)
    											.build();
    	
    	Page<Promocao> page = promocaoService.pesquisar(filtro, pageable);
    	
    	List<PromocaoDTO> payloadPage = promocaoAssembler.toListDto(page.getContent());
    	
		return paginaAssembler.toPage(payloadPage, pageable, page.getTotalElements());
    }
    
    @GetMapping("/{promocaoId}")
    public PromocaoDTO findPromocaoPorId(@PathVariable Long promocaoId) {
    	return promocaoAssembler.toDto(promocaoService.findPromocaoById(promocaoId));
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PromocaoDTO cadastrar(@RequestBody @Valid PromocaoInputDTO promocaoInputDTO) {
    	Promocao promocao = promocaoDisassembler.toEntidade(promocaoInputDTO);
    	
    	promocao = promocaoService.cadastrar(promocao);
    	
    	return promocaoAssembler.toDto(promocao);
    }
    
    @PutMapping("/{promocaoId}")
    public PromocaoDTO atualizar(@RequestBody @Valid PromocaoInputDTO promocaoInputDTO, @PathVariable Long promocaoId) {
    	Promocao promocao = promocaoDisassembler.toEntidade(promocaoInputDTO);
    	
    	promocao = promocaoService.atualizar(promocao, promocaoId);
    	
    	return promocaoAssembler.toDto(promocao);
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{promocaoId}/ativacao")
    public void ativarPromocao(@PathVariable Long promocaoId) {
    	promocaoService.alterarStatusPromocao(promocaoId, StatusPromocao.ATIVA);
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{promocaoId}/ativacao")
    public void desativarPromocao(@PathVariable Long promocaoId) {
    	promocaoService.alterarStatusPromocao(promocaoId, StatusPromocao.INATIVA);
    }
    
	private void validarDatas(LocalDate dataInicio, LocalDate dataFim) {
		if((dataInicio != null && dataFim != null) && dataInicio.isAfter(dataFim))
			throw new RequisicaoInvalidaException("Data ínicio deve ser menor ou igual a data fim");
	}
}
