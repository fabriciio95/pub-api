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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pub.api.dto.produto.ProdutoDTO;
import com.pub.api.dto.promocao.regra.RegraPromocaoDTO;
import com.pub.api.dto.promocao.regra.RegraPromocaoInputDTO;
import com.pub.api.mapper.assembler.PaginaAssembler;
import com.pub.api.mapper.assembler.ProdutoAssembler;
import com.pub.api.mapper.assembler.RegraPromocaoAssembler;
import com.pub.api.mapper.disassembler.RegraPromocaoDisassembler;
import com.pub.domain.model.Produto;
import com.pub.domain.model.RegraPromocao;
import com.pub.domain.model.enums.StatusPromocao;
import com.pub.domain.service.RegraPromocaoService;
import com.pub.domain.service.dto.RegraPromocaoFiltroDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/promocoes/{promocaoId}/regras")
public class RegraPromocaoController {

	private final RegraPromocaoService regraPromocaoService;
	
	private final RegraPromocaoAssembler regraPromocaoAssembler;
	
	private final RegraPromocaoDisassembler regraPromocaoDisassembler;
	
	private final ProdutoAssembler produtoAssembler;
	
	private final PaginaAssembler paginaAssembler;
	
	@GetMapping
	public Page<RegraPromocaoDTO> pesquisar(@PathVariable Long promocaoId,
											RegraPromocaoFiltroDTO filtro,
											Pageable pageable){
		
		Page<RegraPromocao> regras = regraPromocaoService.pesquisar(filtro, pageable);
		
		List<RegraPromocaoDTO> listaRegras = regraPromocaoAssembler.toListDto(regras.getContent());
		
		return paginaAssembler.toPage(listaRegras, pageable, regras.getTotalElements());
	}
	
	@GetMapping("{regraId}")
	public RegraPromocaoDTO findRegraPorId(@PathVariable Long promocaoId, @PathVariable Long regraId) {
		RegraPromocao regraPromocao = regraPromocaoService.findRegraById(promocaoId, regraId);
		
		return regraPromocaoAssembler.toDto(regraPromocao);
	}
	

	@GetMapping("/{regraId}/produtos")
	public Page<ProdutoDTO> findProdutosRegra(@PathVariable Long promocaoId, @PathVariable Long regraId, Pageable pageable) {
		Page<Produto> paginaProdutos = regraPromocaoService.getProdutosRegra(promocaoId, regraId, pageable);
		
		List<ProdutoDTO> produtosDtoLista = produtoAssembler.toListDTO(paginaProdutos.getContent());
		
		return paginaAssembler.toPage(produtosDtoLista, pageable, paginaProdutos.getTotalElements()); 
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public RegraPromocaoDTO cadastrar(@PathVariable Long promocaoId, @RequestBody @Valid RegraPromocaoInputDTO regraPromocaoInputDTO) {
		RegraPromocao regraPromocao = regraPromocaoDisassembler.toEntidade(regraPromocaoInputDTO);
		
		regraPromocao = regraPromocaoService.cadastrar(regraPromocao, promocaoId);
		
		return regraPromocaoAssembler.toDto(regraPromocao);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/{regraId}")
	public RegraPromocaoDTO atualizar(@PathVariable Long promocaoId, @PathVariable Long regraId, @RequestBody @Valid RegraPromocaoInputDTO regraPromocaoInputDTO) {
		RegraPromocao regraPromocao = regraPromocaoDisassembler.toEntidade(regraPromocaoInputDTO);
		
		regraPromocao = regraPromocaoService.atualizar(regraPromocao, regraId, promocaoId);
		
		return regraPromocaoAssembler.toDto(regraPromocao);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/{regraId}/ativacao")
	public void ativarRegra(@PathVariable Long promocaoId, @PathVariable Long regraId) {
		regraPromocaoService.alterarStatus(StatusPromocao.ATIVA, promocaoId, regraId);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{regraId}/ativacao")
	public void desativarRegra(@PathVariable Long promocaoId, @PathVariable Long regraId) {
		regraPromocaoService.alterarStatus(StatusPromocao.INATIVA, promocaoId, regraId);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/{regraId}/produtos/associacao")
	public void associarProdutos(@PathVariable Long promocaoId, @PathVariable Long regraId, @RequestBody List<Long> produtosId) {
		regraPromocaoService.associarProdutos(promocaoId, regraId, produtosId);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{regraId}/produtos/associacao")
	public void desassociarProdutos(@PathVariable Long promocaoId, @PathVariable Long regraId, @RequestBody List<Long> produtosId) {
		regraPromocaoService.desassociarProdutos(promocaoId, regraId, produtosId);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{regraId}")
	public void excluir(@PathVariable Long promocaoId, @PathVariable Long regraId) {
		regraPromocaoService.excluir(promocaoId, regraId);
	}
}
