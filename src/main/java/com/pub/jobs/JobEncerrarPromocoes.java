package com.pub.jobs;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.model.Promocao;
import com.pub.domain.model.enums.StatusPromocao;
import com.pub.domain.service.PromocaoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JobEncerrarPromocoes {

	private final PromocaoService promocaoService;

	@Scheduled(cron = "${app.jobs.job-encerrar-promocoes.cron}")
	@Transactional
	public void encerrarPromocoes() {
		log.info("Iniciando JobEncerrarPromocoes");
		List<Promocao> promocoes = promocaoService.findPromocoesVencidasAtivas();
		
		if(promocoes.isEmpty()) {
			log.info("Não foi encontrada nenhuma promoção para ser desativada");
			return;
		}
		
		promocoes.forEach(promocao -> promocao.setStatus(StatusPromocao.INATIVA));
		
		log.info("Finalizando JobEncerrarPromocoes");
	}
}
