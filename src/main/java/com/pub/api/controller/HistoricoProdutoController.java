package com.pub.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pub.domain.service.HistoricoPrecoProdutoService;
import com.pub.domain.service.HistoricoProdutoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/produtos/historico")
@RestController
public class HistoricoProdutoController {

	private final HistoricoPrecoProdutoService historicoPrecoProdutoService;
	
	private final HistoricoProdutoService historicoProdutoService;
	
	
}
