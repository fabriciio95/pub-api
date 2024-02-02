package com.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.UnidadeConversao;

@Repository
public interface UnidadeConversaoRepository extends JpaRepository<UnidadeConversao, Long>, JpaSpecificationExecutor<UnidadeConversao> {

}
