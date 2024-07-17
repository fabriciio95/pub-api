package com.pub.api.mapper.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.cliente.ClienteDTO;
import com.pub.domain.model.Cliente;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ClienteAssembler {

	private final ModelMapper mapper;
	
	public ClienteDTO toDTO(Cliente cliente) {
		return mapper.map(cliente, ClienteDTO.class);
	}
	
	public List<ClienteDTO> toListDTO(List<Cliente> clientes) {
		return clientes.stream().map(this::toDTO).toList();
	}
}
