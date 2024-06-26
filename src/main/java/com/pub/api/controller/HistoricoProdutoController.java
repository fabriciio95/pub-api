package com.pub.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pub.api.dto.historico.HistoricoProdutoDTO;
import com.pub.api.exception.RequisicaoInvalidaException;
import com.pub.api.mapper.assembler.HistoricoProdutoAssembler;
import com.pub.api.mapper.assembler.PaginaAssembler;
import com.pub.domain.model.HistoricoProduto;
import com.pub.domain.model.Produto;
import com.pub.domain.model.enums.TipoTransacao;
import com.pub.domain.service.HistoricoProdutoService;
import com.pub.domain.service.ProdutoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/produtos/{produtoId}/historico-estoque")
@RestController
public class HistoricoProdutoController {

	private final HistoricoProdutoService historicoProdutoService;
	
	private final PaginaAssembler paginaAssembler;
	
	private final HistoricoProdutoAssembler historicoProdutoAssembler;
	
	private final ProdutoService produtoService;
	
	@GetMapping
	public Page<HistoricoProdutoDTO> obterHistoricoProduto(@PathVariable Long produtoId, Pageable pageable, 
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataInicio, 
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataFim,
			@RequestParam(required = false) TipoTransacao tipoTransacao) {
		Produto produto = produtoService.findProdutoById(produtoId);
		
		if((dataInicio != null && dataFim != null) && dataInicio.isAfter(dataFim))
			throw new RequisicaoInvalidaException("Data ínicio deve ser menor ou igual a data fim");
		
		Page<HistoricoProduto> historicoProduto = historicoProdutoService.listarHistoricoProduto(produto, pageable, dataInicio, dataFim, tipoTransacao);
		
		List<HistoricoProdutoDTO> conteudoPagina = historicoProdutoAssembler.toListDTO(historicoProduto.getContent());
		
		return paginaAssembler.toPage(conteudoPagina, pageable, historicoProduto.getTotalElements());
	}
}
