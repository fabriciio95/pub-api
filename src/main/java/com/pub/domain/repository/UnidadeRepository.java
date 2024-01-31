package com.pub.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.Unidade;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long>, JpaSpecificationExecutor<Unidade> {

	Optional<Unidade> findByNomeIgnoreCase(String descricao);

}
