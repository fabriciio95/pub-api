package com.pub.infrastructure.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.pub.domain.model.Funcionario;
import com.pub.domain.model.enums.FuncaoFuncionario;
import com.pub.domain.model.enums.StatusFuncionario;

public class FuncionarioSpecs {

	public static Specification<Funcionario> comNomeParecido(String nome) {
		return (root, query, criteriaBuilder) -> 
				criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
	}
	
	public static Specification<Funcionario> comTelefoneParecido(String telefone) {
		return (root, query, criteriaBuilder) -> 
				criteriaBuilder.like(criteriaBuilder.lower(root.get("telefone")), "%" + telefone.toLowerCase() + "%");
	}
	
	public static Specification<Funcionario> comCpfParecido(String cpf) {
		return (root, query, criteriaBuilder) -> 
				criteriaBuilder.like(criteriaBuilder.lower(root.get("cpf")), "%" + cpf.toLowerCase() + "%");
	}
	
	public static Specification<Funcionario> comFuncionarioIdIgualA(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
	}
	
	public static Specification<Funcionario> comStatusIgualA(StatusFuncionario status) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
	}
	
	public static Specification<Funcionario> comFuncaoIgualA(FuncaoFuncionario funcao) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("funcao"), funcao);
	}
}
