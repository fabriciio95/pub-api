package com.pub.api.mapper.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.evento.EventoInputDTO;
import com.pub.domain.model.Evento;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class EventoDisassembler {

	private final ModelMapper modelMapper;
	
	public Evento toEntidade(EventoInputDTO eventoInputDTO) {
		return modelMapper.map(eventoInputDTO, Evento.class);
	}
}
