package com.pub.infrastructure.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.HistoricoProduto;
import com.pub.domain.model.enums.TipoTransacao;
import com.pub.domain.repository.custom.HistoricoProdutoQueries;
import com.pub.infrastructure.repository.util.QueryUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class HistoricoProdutoRepositoryImpl implements HistoricoProdutoQueries {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<HistoricoProduto> findHistoricoProduto(Long produtoId, LocalDate dataInicio, LocalDate dataFim,
			TipoTransacao tipoTransacao, Pageable pageable) {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		
		CriteriaQuery<HistoricoProduto> criteria = builder.createQuery(HistoricoProduto.class);
		
		Root<HistoricoProduto> root = criteria.from(HistoricoProduto.class);
		
		root.fetch("perdaAvaria", JoinType.LEFT);
		root.fetch("unidade", JoinType.INNER);
		
		Predicate[] predicates = criarFiltros(produtoId, dataInicio, dataFim, tipoTransacao, builder, root);
		
		criteria.where(predicates);
		
		List<Order> ordenacao = QueryUtils.obterCriteriosOrdenacao(pageable.getSort(), builder, root);
		
		if(!ordenacao.isEmpty()) {
			criteria.orderBy(ordenacao);
		}
		
		TypedQuery<HistoricoProduto> query = em.createQuery(criteria);
		
		QueryUtils.adicionarPaginacaoQuery(query, pageable);
		
		List<HistoricoProduto> historicoProduto = query.getResultList();
		
		return new PageImpl<>(historicoProduto, pageable, totaElementosConsulta(produtoId, dataInicio, dataFim, tipoTransacao));
	}

	private Long totaElementosConsulta(Long produtoId, LocalDate dataInicio, LocalDate dataFim,
			TipoTransacao tipoTransacao) {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<HistoricoProduto> root = criteria.from(HistoricoProduto.class);
		
		Predicate[] predicates = criarFiltros(produtoId, dataInicio, dataFim, tipoTransacao, builder, root);
		
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		
		return em.createQuery(criteria).getSingleResult();
	}

	private Predicate[] criarFiltros(Long produtoId, LocalDate dataInicio, LocalDate dataFim, TipoTransacao tipoTransacao,
			CriteriaBuilder builder, Root<HistoricoProduto> root) {
		List<Predicate> predicates = new ArrayList<>();
		
		predicates.add(builder.equal(root.get("produto").get("id"), produtoId));
		
		if(dataInicio != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("data"), dataInicio.atStartOfDay()));
		}
		
		if(dataFim != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("data"), dataFim.atTime(LocalTime.MAX)));
		}
		
		if(tipoTransacao != null) {
			predicates.add(builder.equal(root.get("tipoTransacao"), tipoTransacao));
		}
		
		return predicates.toArray(new Predicate[0]);
	}
}
