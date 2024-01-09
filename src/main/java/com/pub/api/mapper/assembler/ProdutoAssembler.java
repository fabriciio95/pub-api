package com.pub.api.mapper.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.produto.ProdutoDTO;
import com.pub.domain.model.Produto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ProdutoAssembler {

	private final ModelMapper modelMapper;
	
	public ProdutoDTO toDTO(Produto produto) {
		return modelMapper.map(produto, ProdutoDTO.class);
	}
}
