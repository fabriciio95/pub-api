package com.pub.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pub.api.dto.produto.ProdutoDTO;
import com.pub.api.dto.produto.ProdutoInputDTO;
import com.pub.api.mapper.assembler.ProdutoAssembler;
import com.pub.api.mapper.disassembler.ProdutoDisassembler;
import com.pub.domain.model.Produto;
import com.pub.domain.service.ProdutoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	private final ProdutoService produtoService;
	
	private final ProdutoDisassembler produtoDisassembler;
	
	private final ProdutoAssembler produtoAssembler;
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ProdutoDTO cadastrarProduto(@RequestBody @Valid ProdutoInputDTO produtoInputDTO) {
		Produto produto = produtoDisassembler.toEntidade(produtoInputDTO);
		
		return produtoAssembler.toDTO(produtoService.salvarProduto(produto));
	}
}
