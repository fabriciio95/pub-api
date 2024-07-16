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

import com.pub.api.dto.mesa.MesaDTO;
import com.pub.api.dto.mesa.MesaInputDTO;
import com.pub.api.mapper.assembler.MesaAssembler;
import com.pub.api.mapper.assembler.PaginaAssembler;
import com.pub.api.mapper.disassembler.MesaDisassembler;
import com.pub.domain.model.Mesa;
import com.pub.domain.model.enums.StatusMesa;
import com.pub.domain.service.MesaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/mesas")
@RequiredArgsConstructor
@RestController
public class MesaController {

	private final MesaService mesaService;
	
	private final MesaDisassembler mesaDisassembler;
	
	private final MesaAssembler mesaAssembler;
	
	private final PaginaAssembler paginaAssembler;

	@GetMapping
	public Page<MesaDTO> buscarMesas(
			@RequestParam(required = false) Long mesaId,
			@RequestParam(required = false) Integer numero,
			@RequestParam(required = false) StatusMesa status,
			@RequestParam(required = false) Integer capacidade,
			Pageable pageable) {
		
		Page<Mesa> mesas = mesaService.pesquisar(mesaId, numero, status, capacidade, pageable);
		
		List<MesaDTO> contentPage = mesaAssembler.toListDTO(mesas.getContent());
		
		return paginaAssembler.toPage(contentPage, pageable, mesas.getTotalElements());
	}
	
	@GetMapping("/{mesaId}")
	public MesaDTO findById(@PathVariable Long mesaId) {
		Mesa mesa = mesaService.findMesaById(mesaId);
		
		return mesaAssembler.toDTO(mesa);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public MesaDTO cadastrar(@RequestBody @Valid MesaInputDTO mesaInputDto) {
		
		Mesa mesa = mesaDisassembler.toEntidade(mesaInputDto);
		
		mesa = mesaService.cadastrar(mesa);
		
		return mesaAssembler.toDTO(mesa);
	}
	
	@PutMapping("/{mesaId}")
	public MesaDTO atualizar(@PathVariable Long mesaId, @RequestBody @Valid MesaInputDTO mesaInputDTO) {
		
		Mesa mesa = mesaDisassembler.toEntidade(mesaInputDTO);
		
		mesa = mesaService.atualizar(mesaId, mesa);
		
		return mesaAssembler.toDTO(mesa);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{mesaId}")
	public void excluir(@PathVariable Long mesaId) {
		mesaService.excluir(mesaId);
	}
}
