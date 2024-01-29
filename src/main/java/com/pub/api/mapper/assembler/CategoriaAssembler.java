package com.pub.api.mapper.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.categoria.CategoriaDTO;
import com.pub.domain.model.Categoria;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CategoriaAssembler {

	private final ModelMapper modelMapper;
	
	public CategoriaDTO toDto(Categoria categoria) {
		return modelMapper.map(categoria, CategoriaDTO.class);
	}
	
	public List<CategoriaDTO> toListDto(List<Categoria> categorias) {
		return categorias.stream().map(c -> modelMapper.map(c, CategoriaDTO.class)).toList();
	}
}
