package com.pub.api.mapper.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pub.api.dto.promocao.regra.RegraPromocaoInputDTO;
import com.pub.domain.model.RegraPromocao;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RegraPromocaoDisassembler {

	private final ModelMapper modelMapper;
	
	public RegraPromocao toEntidade(RegraPromocaoInputDTO regraDTO) {
		RegraPromocao regraPromocao =  modelMapper.map(regraDTO, RegraPromocao.class);
		
//		if(regraDTO.getProdutoGratisId() != null) {
//			Produto produto = new Produto();
//			produto.setId(regraDTO.getProdutoGratisId());
//			regraPromocao.setProdutoGratis(produto);
//		}
		
		return regraPromocao;
	}
}
