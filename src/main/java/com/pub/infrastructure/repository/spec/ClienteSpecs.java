package com.pub.infrastructure.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.pub.domain.model.Cliente;

public class ClienteSpecs {

	public static Specification<Cliente> comNomeParecido(String nome) {
		return (root, query, criteriaBuilder) -> 
				criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
	}
	
	public static Specification<Cliente> comTelefoneParecido(String telefone) {
		return (root, query, criteriaBuilder) -> 
				criteriaBuilder.like(criteriaBuilder.lower(root.get("telefone")), "%" + telefone.toLowerCase() + "%");
	}
	
	public static Specification<Cliente> comCpfParecido(String cpf) {
		return (root, query, criteriaBuilder) -> 
				criteriaBuilder.like(criteriaBuilder.lower(root.get("cpf")), "%" + cpf.toLowerCase() + "%");
	}
	
	public static Specification<Cliente> comClienteIdIgualA(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
	}
}
