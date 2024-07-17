package com.pub.api.mapper.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.cliente.ClienteInputDTO;
import com.pub.domain.model.Cliente;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ClienteDisassembler {

	private final ModelMapper modelMapper;
	
	public Cliente toEntidade(ClienteInputDTO clienteInputDTO) {
		return modelMapper.map(clienteInputDTO, Cliente.class);
	}
}
