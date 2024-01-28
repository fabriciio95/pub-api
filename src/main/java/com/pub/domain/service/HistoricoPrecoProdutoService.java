package com.pub.domain.service;

import static com.pub.infrastructure.repository.spec.HistoricoPrecoProdutoSpecs.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pub.domain.model.HistoricoPrecoProduto;
import com.pub.domain.model.Produto;
import com.pub.domain.repository.HistoricoPrecoProdutoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HistoricoPrecoProdutoService {

	private final HistoricoPrecoProdutoRepository historicoPrecoProdutoRepository;
	
	@Transactional
	public HistoricoPrecoProduto salvarHistoricoPrecoProduto(Produto produto) {
		HistoricoPrecoProduto historicoPrecoProduto = HistoricoPrecoProduto.builder()
				                                                              .data(OffsetDateTime.now())
				                                                              .preco(produto.getPreco())
				                                                              .produto(produto)
				                                                           .build();
		
		return historicoPrecoProdutoRepository.save(historicoPrecoProduto);
	}
	
	public Page<HistoricoPrecoProduto> listarHistoricoPrecoProduto(Produto produto, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
		
		Specification<HistoricoPrecoProduto> spec = comProdutoIdIgualA(produto.getId());
		
		if(dataInicio != null)
			spec = spec.and(comDataMaiorOuIgualA(dataInicio));
		
		if(dataFim != null)
			spec = spec.and(comDataMenorOuIgualA(dataFim));
		
		return historicoPrecoProdutoRepository.findAll(spec, pageable);
	}
}
