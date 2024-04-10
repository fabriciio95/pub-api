package com.pub.jobs;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.model.Evento;
import com.pub.domain.model.enums.StatusEvento;
import com.pub.domain.service.EventoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JobIniciarEventos {

	private final EventoService eventoService;

	@Scheduled(cron = "${app.jobs.job-iniciar-eventos.cron}")
	@Transactional
	public void iniciarEventos() {
		log.info("Iniciando JobIniciarEventos");
		List<Evento> eventos = eventoService.findEventosAtivosIniciados(OffsetDateTime.now());
		
		if(eventos.isEmpty()) {
			log.info("Não foi encontrado nenhum evento para ser iniciado");
			return;
		}
		
		eventos.forEach(evento -> evento.setStatus(StatusEvento.INICIADO));
		
		log.info("Finalizando JobIniciarEventos");
	}
}
