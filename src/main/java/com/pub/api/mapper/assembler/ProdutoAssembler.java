package com.pub.api.mapper.assembler;

import java.util.List;

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
	
	public List<ProdutoDTO> toListDTO(List<Produto> produtos) {
		return produtos.stream().map(p -> modelMapper.map(p, ProdutoDTO.class)).toList();
	}
}
