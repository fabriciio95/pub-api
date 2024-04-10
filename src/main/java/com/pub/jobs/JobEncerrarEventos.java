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
public class JobEncerrarEventos {

	private final EventoService eventoService;

	@Scheduled(cron = "${app.jobs.job-encerrar-eventos.cron}")
	@Transactional
	public void encerrarEventos() {
		log.info("Iniciando JobEncerrarEventos");
		List<Evento> eventos = eventoService.findEventosDisponivelAnteriores();
		
		if(eventos.isEmpty()) {
			log.info("Não foi encontrado nenhum evento para ser encerrado");
			return;
		}
		
		eventos.forEach(evento -> {
			if(evento.getStatus().equals(StatusEvento.INICIADO)) {
				log.info("Atualizando status do evento {} de {} para {}", evento.getId(), StatusEvento.INICIADO, StatusEvento.FINALIZADO);
				evento.setStatus(StatusEvento.FINALIZADO);
				evento.setDataHoraFinalizacao(OffsetDateTime.now());
			} else {
				log.info("Atualizando status do evento {} de {} para {}", evento.getId(), evento.getStatus(), StatusEvento.CANCELADO);
				evento.setStatus(StatusEvento.CANCELADO);
				evento.setDataHoraCancelamento(OffsetDateTime.now());
			}
		});
		
		log.info("Finalizando JobFinalizarEventos");
	}
}
