package com.pub.infrastructure.repository.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public class QueryUtils {

	public static void adicionarPaginacaoQuery(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int registrosPorPagina = pageable.getPageSize();
		int primeiroRegistroPagina = paginaAtual * registrosPorPagina;
		
		query.setFirstResult(primeiroRegistroPagina);
		query.setMaxResults(registrosPorPagina);
	}
	
	public static List<Order> obterCriteriosOrdenacao(Sort sort, CriteriaBuilder builder, Root<?> root) {
		List<Order> orders = new ArrayList<>();
		
		if(sort == null || builder == null || root == null)
			return orders;
		
		for(Sort.Order order : sort) {
			
			String campo = order.getProperty();
			
			Order criteriaOrder = null;
			
			if(order.isAscending()) {
				criteriaOrder = builder.asc(root.get(campo));
			} else {
				criteriaOrder = builder.desc(root.get(campo));
			}
			
			orders.add(criteriaOrder);
		}
		
		return orders;
	}
}
