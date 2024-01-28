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

import com.pub.api.dto.historico.HistoricoPrecoProdutoDTO;
import com.pub.api.exception.RequisicaoInvalidaException;
import com.pub.api.mapper.assembler.HistoricoPrecoProdutoAssembler;
import com.pub.api.mapper.assembler.PaginaAssembler;
import com.pub.domain.model.HistoricoPrecoProduto;
import com.pub.domain.model.Produto;
import com.pub.domain.service.HistoricoPrecoProdutoService;
import com.pub.domain.service.ProdutoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/produtos/{produtoId}/historico-preco")
@RestController
public class HistoricoPrecoProdutoController {

	private final HistoricoPrecoProdutoService historicoPrecoProdutoService;
	
	private final HistoricoPrecoProdutoAssembler historicoPrecoProdutoAssembler;
	
	private final PaginaAssembler paginaAssembler;
	
	private final ProdutoService produtoService;
	
	
	@GetMapping
	public Page<HistoricoPrecoProdutoDTO> obterHistoricoPrecoProduto(@PathVariable Long produtoId,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataFim,
			Pageable pageable) {
		Produto produto = produtoService.findProdutoById(produtoId);
		
		if((dataInicio != null && dataFim != null) && dataInicio.isAfter(dataFim))
			throw new RequisicaoInvalidaException("Data ínicio deve ser menor ou igual a data fim");
		
		Page<HistoricoPrecoProduto> historicoPrecoProduto = historicoPrecoProdutoService.listarHistoricoPrecoProduto(produto, dataInicio, dataFim, pageable);
		
		List<HistoricoPrecoProdutoDTO> conteudoPagina = historicoPrecoProdutoAssembler.toListDTO(historicoPrecoProduto.getContent());
		
		return paginaAssembler.toPage(conteudoPagina, pageable, historicoPrecoProduto.getTotalElements());
	}
}
