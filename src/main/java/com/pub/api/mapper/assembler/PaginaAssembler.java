package com.pub.api.mapper.assembler;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PaginaAssembler {
	
	public <T> Page<T> toPage(List<T> content, Pageable pageable, long totalElements) {
		return new PageImpl<>(content, pageable, totalElements);
	}
}
