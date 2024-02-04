package com.pub.api.controller;

import java.util.List;
import java.util.Set;

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

import com.pub.api.dto.categoria.CategoriaDTO;
import com.pub.api.dto.categoria.CategoriaInputDTO;
import com.pub.api.dto.unidadeConversao.UnidadeConversaoDTO;
import com.pub.api.mapper.assembler.CategoriaAssembler;
import com.pub.api.mapper.assembler.PaginaAssembler;
import com.pub.api.mapper.assembler.UnidadeConversaoAssembler;
import com.pub.api.mapper.disassembler.CategoriaDisassembler;
import com.pub.domain.model.Categoria;
import com.pub.domain.model.UnidadeConversao;
import com.pub.domain.service.CategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/categorias")
@RestController
public class CategoriaController {

	private final CategoriaService categoriaService;
	
	private final CategoriaAssembler categoriaAssembler;
	
	private final CategoriaDisassembler categoriaDisassembler;
	
	private final PaginaAssembler paginaAssembler;
	
	private final UnidadeConversaoAssembler unidadeConversaoAssembler;
	
	@GetMapping("{categoriaId}")
	public CategoriaDTO findCategoriaPorId(@PathVariable Long categoriaId) {
		return categoriaAssembler.toDto(categoriaService.findCategoriaById(categoriaId));
	}
	
	@GetMapping("/{categoriaId}/conversoes")
	public List<UnidadeConversaoDTO> findConversoesUnidadePorId(@PathVariable Long categoriaId, Pageable pageable) {
	    Set<UnidadeConversao> conversoesCategoria = categoriaService.listarConversoesCategoria(categoriaId);

	    return unidadeConversaoAssembler.toListDto(conversoesCategoria);
	}
	
	@GetMapping
	public Page<CategoriaDTO> listarCategorias(@RequestParam(required = false) String nome, Pageable pageable) {
		 Page<Categoria> categorias = categoriaService.listarCategorias(nome, pageable);
		 
		 List<CategoriaDTO> categoriasDto = categoriaAssembler.toListDto(categorias.getContent());
		 
		 return paginaAssembler.toPage(categoriasDto, pageable, categorias.getTotalElements());
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public CategoriaDTO cadastrarCategoria(@RequestBody @Valid CategoriaInputDTO categoriaInputDTO) {
		Categoria categoria = categoriaDisassembler.toEntidade(categoriaInputDTO);
		
		return categoriaAssembler.toDto(categoriaService.cadastrarCategoria(categoria));
	}
	
	@PutMapping("/{categoriaId}")
	public CategoriaDTO atualizarCategoria(@RequestBody @Valid CategoriaInputDTO categoriaInputDTO, @PathVariable Long categoriaId) {
		Categoria categoria = categoriaDisassembler.toEntidade(categoriaInputDTO);
		
		return categoriaAssembler.toDto(categoriaService.atualizarCategoria(categoria, categoriaId));
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/{categoriaId}/associacao-conversao")
	public void associarConversao(@RequestBody List<Long> conversoesIds, @PathVariable Long categoriaId) {
	    categoriaService.associarConversoes(categoriaId, conversoesIds);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{categoriaId}/associacao-conversao")
	public void desassociarConversao(@RequestBody List<Long> conversoesIds, @PathVariable Long categoriaId) {
		categoriaService.desassociarConversoes(categoriaId, conversoesIds);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{categoriaId}")
	public void excluirUnidadePorId(@PathVariable Long categoriaId) {
		categoriaService.excluirCategoria(categoriaId);
	}
}
