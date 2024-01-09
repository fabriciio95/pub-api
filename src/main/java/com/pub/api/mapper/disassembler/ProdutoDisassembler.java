package com.pub.api.mapper.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.produto.ProdutoInputDTO;
import com.pub.domain.model.Produto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ProdutoDisassembler {

	private final ModelMapper modelMapper;
	
	public Produto toEntidade(ProdutoInputDTO produtoInputDTO) {
		return modelMapper.map(produtoInputDTO, Produto.class);
	}
	
}
