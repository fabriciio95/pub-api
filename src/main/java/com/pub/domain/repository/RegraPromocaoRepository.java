package com.pub.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pub.domain.model.Produto;
import com.pub.domain.model.RegraPromocao;


@Repository
public interface RegraPromocaoRepository extends JpaRepository<RegraPromocao, Long>, JpaSpecificationExecutor<RegraPromocao> {

	Optional<RegraPromocao> findByPromocaoIdAndId(Long promocaoId, Long regraId);
	
	@Query("SELECT p FROM RegraPromocao rp JOIN rp.produtos p WHERE rp.id = :id")
    Page<Produto> findProdutosByRegraPromocaoId(@Param("id") Long id, Pageable pageable);
}
