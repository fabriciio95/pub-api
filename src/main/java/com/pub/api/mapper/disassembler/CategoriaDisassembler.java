package com.pub.api.mapper.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.categoria.CategoriaInputDTO;
import com.pub.domain.model.Categoria;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CategoriaDisassembler {

	private final ModelMapper modelMapper;
	
	public Categoria toEntidade(CategoriaInputDTO categoriaInputDTO) {
		return modelMapper.map(categoriaInputDTO, Categoria.class);
	}
}
