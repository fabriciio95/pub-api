package com.pub.infrastructure.repository.spec;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.data.jpa.domain.Specification;

import com.pub.domain.model.Evento;
import com.pub.domain.model.enums.StatusEvento;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;

public class EventoSpecs {
	
	public static Specification<Evento> comDataInicioMaiorOuIgualA(OffsetDateTime data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dataHoraInicioEvento"), data);
	}
	
	public static Specification<Evento> comDataInicioMaiorOuIgualA(LocalDate data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dataHoraInicioEvento"), data.atStartOfDay().atOffset(ZoneOffset.UTC));
	}
	
	public static Specification<Evento> comDataInicioMenorOuIgualA(OffsetDateTime data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("dataHoraInicioEvento"), data);
	}
	
	public static Specification<Evento> comDataInicioMenorOuIgualA(LocalDate data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("dataHoraInicioEvento"),  data.atTime(LocalTime.MAX));
	}
	
	public static Specification<Evento> comDataFimMenorOuIgualA(OffsetDateTime data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("dataHoraFimEvento"), data);
	}
	
	public static Specification<Evento> comDataFimMenorOuIgualA(LocalDate data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("dataHoraFimEvento"),  data.atTime(LocalTime.MAX));
	}
	
	public static Specification<Evento> comDataFimMenorQue(OffsetDateTime data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("dataHoraFimEvento"),  data);
	}
	
	public static Specification<Evento> comDataFimMaiorOuIgualA(OffsetDateTime data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dataHoraFimEvento"), data);
	}
	
	public static Specification<Evento> comDataFimMaiorOuIgualA(LocalDate data) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dataHoraFimEvento"), data.atStartOfDay().atOffset(ZoneOffset.UTC));
	}
	
	public static Specification<Evento> comIdDiferenteDe(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("id"), id);
	}
	
	public static Specification<Evento> comIdIgualA(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
	}
	
	public static Specification<Evento> comStatusIgualA(StatusEvento status) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
	}
	
	public static Specification<Evento> contemStatusIgualA(StatusEvento... status) {
		return (root, query, criteriaBuilder) -> {
			Path<StatusEvento> statusPath = root.get("status");
			
			CriteriaBuilder.In<StatusEvento> clausulaIn = criteriaBuilder.in(statusPath);
			
			for(StatusEvento statusEvento : status) {
				clausulaIn = clausulaIn.value(statusEvento);
			}
			
			return clausulaIn;
		};
	}
	
	public static Specification<Evento> comDescricaoParecida(String descricao) {
		return (root, query, criteriaBuilder) -> 
				criteriaBuilder.like(criteriaBuilder.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%");
	}
	
	public static Specification<Evento> comDataInicioEFimValida(OffsetDateTime dataInicio, OffsetDateTime dataFim) {
		return comDataInicioMaiorOuIgualA(dataInicio)
				.and(comDataInicioMenorOuIgualA(dataFim))
				.or(comDataFimMaiorOuIgualA(dataFim)
		        .and(comDataFimMenorOuIgualA(dataFim)));
	}
	
	public static Specification<Evento> comStatusAtivoOuIniciado() {
		return comStatusIgualA(StatusEvento.ATIVO).or(comStatusIgualA(StatusEvento.INICIADO));
	}
}
