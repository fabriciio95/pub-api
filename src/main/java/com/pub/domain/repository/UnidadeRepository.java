package com.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.Unidade;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long>{

}
