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

import com.pub.api.dto.produto.AlteracaoEstoqueDTO;
import com.pub.api.dto.produto.ProdutoDTO;
import com.pub.api.dto.produto.ProdutoIdDTO;
import com.pub.api.dto.produto.ProdutoInputDTO;
import com.pub.api.mapper.assembler.PaginaAssembler;
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
	
	private final PaginaAssembler paginaAssembler;
	
	@GetMapping
	public Page<ProdutoDTO> pesquisarProdutos(@RequestParam(required = false) Long produtoId,
			                                  @RequestParam(required = false) String nome,
			                                  @RequestParam(required = false) Boolean ativo,
			                                  @RequestParam(required = false) Long categoriaId,
			                                  @RequestParam(required = false) Long unidadeId,
			                                  Pageable pageable) {
		Page<Produto> produtos = produtoService.pesquisarProdutos(produtoId, nome, ativo, categoriaId, unidadeId, pageable);
		
		List<ProdutoDTO> content = produtoAssembler.toListDTO(produtos.getContent());
		
		return paginaAssembler.toPage(content, pageable, produtos.getTotalElements());
	}
	
	@GetMapping("/{produtoId}")
	public ProdutoDTO findProdutoPorId(@PathVariable Long produtoId) {
		Produto produto = produtoService.findProdutoById(produtoId);
		
		return produtoAssembler.toDTO(produto);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ProdutoDTO cadastrarProduto(@RequestBody @Valid ProdutoInputDTO produtoInputDTO) {
		Produto produto = produtoDisassembler.toEntidade(produtoInputDTO);
		
		Long unidadeConversaoId = produtoInputDTO.getUnidadeConversao() != null && produtoInputDTO.getUnidadeConversao().getId() != null ? 
				produtoInputDTO.getUnidadeConversao().getId() : null;
		
		return produtoAssembler.toDTO(produtoService.cadastrarProduto(produto, unidadeConversaoId, produtoInputDTO.getValorTotal()));
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
