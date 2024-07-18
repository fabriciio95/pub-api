package com.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.Funcionario;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>, JpaSpecificationExecutor<Funcionario> {

	boolean existsByTelefone(String cpf);
	
	boolean existsByTelefoneAndIdNot(String cpf, Long id);
	
	boolean existsByCpf(String cpf);
	
	boolean existsByCpfAndIdNot(String cpf, Long id);
}
