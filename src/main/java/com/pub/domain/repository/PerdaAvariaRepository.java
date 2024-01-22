package com.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.PerdaAvaria;

@Repository
public interface PerdaAvariaRepository extends JpaRepository<PerdaAvaria, Long>{

}
