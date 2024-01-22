package com.pub.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pub.api.dto.produto.AlteracaoEstoqueDTO;
import com.pub.api.dto.produto.ProdutoDTO;
import com.pub.api.dto.produto.ProdutoIdDTO;
import com.pub.api.dto.produto.ProdutoInputDTO;
import com.pub.api.mapper.assembler.ProdutoAssembler;
import com.pub.api.mapper.disassembler.ProdutoDisassembler;
import com.pub.api.mapper.disassembler.TransacaoEstoqueDisassembler;
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
	
	private final TransacaoEstoqueDisassembler transacaoEstoqueDisassembler;
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ProdutoDTO cadastrarProduto(@RequestBody @Valid ProdutoInputDTO produtoInputDTO) {
		Produto produto = produtoDisassembler.toEntidade(produtoInputDTO);
		
		return produtoAssembler.toDTO(produtoService.cadastrarProduto(produto));
	}
	
	@PutMapping("/{produtoId}")
	public ProdutoDTO atualizarProduto(@RequestBody @Valid ProdutoInputDTO produtoInputDTO, @PathVariable Long produtoId) {
		Produto produto = produtoDisassembler.toEntidade(produtoInputDTO);
		
		return produtoAssembler.toDTO(produtoService.atualizarProduto(produto, produtoId));
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/{produtoId}/ativo")
	public void ativarProduto(@PathVariable Long produtoId) {
		produtoService.alterarStatusAtivacaoProduto(produtoId, true);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{produtoId}/ativo")
	public void inativar(@PathVariable Long produtoId) {
		produtoService.alterarStatusAtivacaoProduto(produtoId, false);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/{produtoId}/alteracao-estoque")
	public void registrarAlteracaoEstoque(@PathVariable Long produtoId, @RequestBody AlteracaoEstoqueDTO alteracaoEstoqueDTO) {
		alteracaoEstoqueDTO.setProduto(new ProdutoIdDTO(produtoId));
		
		produtoService.registrarAlteracaoEstoque(transacaoEstoqueDisassembler.toTransacaoEstoqueDTO(alteracaoEstoqueDTO));
	}
	
}
