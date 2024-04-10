package com.pub.api.mapper.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.evento.EventoDTO;
import com.pub.domain.model.Evento;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class EventoAssembler {

	private final ModelMapper modelMapper;
	
	public EventoDTO toDto(Evento evento) {
		return modelMapper.map(evento, EventoDTO.class);
	}
	
	public List<EventoDTO> toListDto(List<Evento> eventos) {
		return eventos.stream().map(this::toDto).toList();
	}
}
