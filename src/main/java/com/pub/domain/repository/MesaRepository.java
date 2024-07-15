package com.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.Mesa;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long>, JpaSpecificationExecutor<Mesa> {

}
