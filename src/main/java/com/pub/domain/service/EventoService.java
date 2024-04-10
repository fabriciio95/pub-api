package com.pub.domain.service;

import static com.pub.infrastructure.repository.spec.EventoSpecs.comDataFimMaiorOuIgualA;
import static com.pub.infrastructure.repository.spec.EventoSpecs.comDataFimMenorOuIgualA;
import static com.pub.infrastructure.repository.spec.EventoSpecs.comDataFimMenorQue;
import static com.pub.infrastructure.repository.spec.EventoSpecs.comDataInicioEFimValida;
import static com.pub.infrastructure.repository.spec.EventoSpecs.comDataInicioMaiorOuIgualA;
import static com.pub.infrastructure.repository.spec.EventoSpecs.comDataInicioMenorOuIgualA;
import static com.pub.infrastructure.repository.spec.EventoSpecs.comDescricaoParecida;
import static com.pub.infrastructure.repository.spec.EventoSpecs.comIdDiferenteDe;
import static com.pub.infrastructure.repository.spec.EventoSpecs.comIdIgualA;
import static com.pub.infrastructure.repository.spec.EventoSpecs.comStatusAtivoOuIniciado;
import static com.pub.infrastructure.repository.spec.EventoSpecs.comStatusIgualA;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.exception.EntidadeNaoEncontradaException;
import com.pub.domain.exception.ObjetoConflitanteException;
import com.pub.domain.exception.ViolacaoRegraNegocioException;
import com.pub.domain.model.Evento;
import com.pub.domain.model.enums.StatusEvento;
import com.pub.domain.repository.EventoRepository;
import com.pub.domain.service.dto.EventoFiltroDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventoService {

	private final EventoRepository eventoRepository;
	
	@Transactional
	public Page<Evento> pesquisar(EventoFiltroDTO filtro, Pageable pageable) {
		
		Specification<Evento> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
		
		if(filtro.getDataInicioPeriodoInicio() != null) {
			spec = spec.and(comDataInicioMaiorOuIgualA(filtro.getDataInicioPeriodoInicio()));
		}
		
		if(filtro.getDataInicioPeriodoFim() != null) {
			spec = spec.and(comDataInicioMenorOuIgualA(filtro.getDataInicioPeriodoFim()));
		}
		
		if(filtro.getDataFimPeriodoInicio() != null) {
			spec = spec.and(comDataFimMaiorOuIgualA(filtro.getDataFimPeriodoInicio()));
		}
		
		if(filtro.getDataFimPeriodoFim() != null) {
			spec = spec.and(comDataFimMenorOuIgualA(filtro.getDataFimPeriodoFim()));
		}
		
		if(filtro.getDescricao() != null) {
			spec = spec.and(comDescricaoParecida(filtro.getDescricao()));
		}
		
		if(filtro.getId() != null) {
			spec = spec.and(comIdIgualA(filtro.getId()));
		}
		
		if(filtro.getStatus() != null) {
			spec = spec.and(comStatusIgualA(filtro.getStatus()));
		}
		
		return eventoRepository.findAll(spec, pageable);
	}
	
	@Transactional
	public Evento cadastrar(Evento evento) {
		validarDataEvento(evento);
		
		List<Evento> eventosCadastrados = eventoRepository.findAll(comDataInicioEFimValida(evento.getDataHoraInicioEvento(),  evento.getDataHoraFimEvento())
                .and(comStatusAtivoOuIniciado()));

		if(eventosCadastrados.size() > 0) {
			throw new ViolacaoRegraNegocioException("Já existem eventos cadastrados para o mesmo período");
		}
		
		evento.setStatus(StatusEvento.ATIVO);
		
		return eventoRepository.save(evento);
	}
	
	@Transactional
	public Evento atualizar(Evento evento, Long eventoId) {
		Evento eventoCadastrado = findEventoById(eventoId);
		
		validarDataEvento(eventoCadastrado);
		
		List<StatusEvento> statusPossiveisParaAtualizacao = eventoCadastrado.getStatus().getStatusPossiveisParaMudanca();
		
		if(!statusPossiveisParaAtualizacao.contains(evento.getStatus())) {
			throw new ViolacaoRegraNegocioException(String.format("Evento de código %d não pode ser atualizado para o status %s, apenas para os status ( %s )",
					eventoId, evento.getStatus(), statusPossiveisParaAtualizacao.stream()
					.map(StatusEvento::name)
					.collect(Collectors.joining(", "))));
		}
		
		eventoCadastrado.setStatus(evento.getStatus());
		
		Specification<Evento> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
		
		if(eventoCadastrado.getStatus().equals(StatusEvento.ATIVO) || eventoCadastrado.getStatus().equals(StatusEvento.INICIADO)) {
			spec = comDataInicioEFimValida(evento.getDataHoraInicioEvento(),  evento.getDataHoraFimEvento())
                    .and(comStatusAtivoOuIniciado())
			        .and(comIdDiferenteDe(eventoId));
		
			List<Evento> eventosCadastrados = eventoRepository.findAll(spec);
	
			if(eventosCadastrados.size() > 0) {
				throw new ViolacaoRegraNegocioException("Já existem eventos cadastrados para o mesmo período");
			}
		}
		
		preencherDataAlteracaoStatus(eventoCadastrado);
		
		BeanUtils.copyProperties(evento, eventoCadastrado, "id", "dataCadastro", "dataAtualizacao", "status");
		
		return eventoCadastrado;
	}
	
	private void preencherDataAlteracaoStatus(Evento eventoCadastrado) {
		 if(eventoCadastrado.getStatus().equals(StatusEvento.CANCELADO) && eventoCadastrado.getDataHoraCancelamento() == null) {
			eventoCadastrado.setDataHoraCancelamento(OffsetDateTime.now());
		} else if(eventoCadastrado.getStatus().equals(StatusEvento.FINALIZADO) && eventoCadastrado.getDataHoraFinalizacao() == null) {
			eventoCadastrado.setDataHoraFinalizacao(OffsetDateTime.now());
		}
	}

	@Transactional
	public List<Evento> findEventosDisponivelAnteriores() {
	   return eventoRepository.findAll(comStatusAtivoOuIniciado().and(comDataFimMenorQue(OffsetDateTime.now())));
	}
	
	@Transactional
	public List<Evento> findEventosAtivosIniciados(OffsetDateTime data) {
	   return eventoRepository.findAll(comStatusIgualA(StatusEvento.ATIVO).and(comDataInicioMenorOuIgualA(data)));
	}
	
	@Transactional
	public void excluirEvento(Long eventoId) {
		try {
			Evento evento = findEventoById(eventoId);
			
			eventoRepository.delete(evento);
			
			eventoRepository.flush();
			
		} catch(DataIntegrityViolationException ex) {
			throw new ObjetoConflitanteException(String.format("Evento de código %d possui lançamentos vinculados, portanto não pode ser excluído", eventoId));
		}
	}

	@Transactional
	public Evento findEventoById(Long eventoId) {
		return eventoRepository.findById(eventoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Evento de código %d não encontrado", eventoId)));
	}
	
	@Transactional
	public boolean existsEventoById(Long eventoId) {
		return eventoRepository.existsById(eventoId);
	}
	
	
	private void validarDataEvento(Evento evento) {
		if(evento.getDataHoraInicioEvento().isAfter(evento.getDataHoraFimEvento())) {
			throw new ViolacaoRegraNegocioException("Data de início do evento não pode ser depois da data do fim");
		}
		
		if(evento.getDataHoraFimEvento().isBefore(OffsetDateTime.now()) || evento.getDataHoraFimEvento().isEqual(OffsetDateTime.now())) {
			throw new ViolacaoRegraNegocioException("Data fim do evento não pode ser depois antes ou igual a data atual");
		}
	}
}
