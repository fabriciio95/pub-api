package com.pub.domain.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.exception.EntidadeNaoEncontradaException;
import com.pub.domain.exception.ViolacaoRegraNegocioException;
import com.pub.domain.model.Promocao;
import com.pub.domain.model.enums.StatusPromocao;
import com.pub.domain.repository.PromocaoRepository;
import com.pub.domain.service.dto.PromocaoFiltroDTO;
import static com.pub.infrastructure.repository.spec.PromocaoSpecs.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromocaoService {

	private final PromocaoRepository promocaoRepository;
	
	
	@Transactional
	public Page<Promocao> pesquisar(PromocaoFiltroDTO filtro, Pageable pageable) {
		
		Specification<Promocao> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
		
		if(filtro.getDataInicio() != null) {
			spec = spec.and(comDataInicioMaiorOuIgualA(filtro.getDataInicio()));
		}
		
		if(filtro.getDataFim() != null) {
			spec = spec.and(comDataFimMenorOuIgualA(filtro.getDataFim()));
		}
		
		if(filtro.getDescricao() != null) {
			spec = spec.and(comDescricaoParecida(filtro.getDescricao()));
		}
		
		if(filtro.getId() != null) {
			spec = spec.and(comIdIgualA(filtro.getId()));
		}
		
		if(filtro.getStatus() != null) {
			spec = spec.and(comStatusIgualA(filtro.getStatus()));
		}
		
		return promocaoRepository.findAll(spec, pageable);
	}
	
	@Transactional
	public Promocao cadastrar(Promocao promocao) {
		
		validarDataPromocao(promocao);
		
		promocao.setStatus(StatusPromocao.ATIVA);
		
		return promocaoRepository.save(promocao);
	}
	
	@Transactional
	public Promocao atualizar(Promocao promocao, Long promocaoId) {
		Promocao promocaoCadastrada = findPromocaoById(promocaoId);
		
		validarDataPromocao(promocao);
		
		BeanUtils.copyProperties(promocao, promocaoCadastrada, "id", "dataCadastro", "dataAtualizacao", "status");
		
		return promocaoCadastrada;
	}
	
	@Transactional
	public void alterarStatusPromocao(Long promocaoId, StatusPromocao status) {
		Promocao promocao = findPromocaoById(promocaoId);
		
		validarDataPromocao(promocao);
		
		promocao.setStatus(status);
	}
	
	@Transactional
	public List<Promocao> findPromocoesVencidasAtivas() {
		return this.promocaoRepository.findAll(comDataFimMenorOuIgualA(LocalDate.now()).and(comStatusIgualA(StatusPromocao.ATIVA)));
	}
	
	
	@Transactional
	public Promocao findPromocaoById(Long promocaoId) {
		return promocaoRepository.findById(promocaoId)
				 .orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Promocão de código %d não encontrada", promocaoId)));
	}
	
	
	private void validarDataPromocao(Promocao promocao) {
		if(promocao.getDataInicio().isAfter(promocao.getDataFim())) {
			throw new ViolacaoRegraNegocioException("Data de início da promoção não pode ser depois da data do fim");
		}
		
		if(promocao.getDataFim().isBefore(OffsetDateTime.now()) || promocao.getDataFim().isEqual(OffsetDateTime.now())) {
			throw new ViolacaoRegraNegocioException("Data fim da promoção não pode ser depois antes ou igual a data atual");
		}
	}
}
