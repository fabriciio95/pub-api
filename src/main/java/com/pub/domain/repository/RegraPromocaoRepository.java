package com.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.RegraPromocao;

@Repository
public interface RegraPromocaoRepository extends JpaRepository<RegraPromocao, Long>, JpaSpecificationExecutor<RegraPromocao> {

}
