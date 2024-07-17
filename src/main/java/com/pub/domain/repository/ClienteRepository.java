package com.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente>{

	boolean existsByTelefone(String cpf);
	
	boolean existsByTelefoneAndIdNot(String cpf, Long id);
	
	boolean existsByCpf(String cpf);
	
	boolean existsByCpfAndIdNot(String cpf, Long id);
}
