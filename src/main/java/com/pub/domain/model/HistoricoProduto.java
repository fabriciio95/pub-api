package com.pub.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

import com.pub.domain.model.enums.TipoTransacao;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class HistoricoProduto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	private OffsetDateTime data;
	
	private BigDecimal preco;
	
	private Integer quantidade;
	
	@Enumerated(EnumType.STRING)
	private TipoTransacao tipoTransacao;
	
	@ManyToOne
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;
	
	@ManyToOne
	@JoinColumn(name = "unidade_id", nullable = false)
	private Unidade unidade;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "perda_avaria_id", nullable = true)
	private PerdaAvaria perdaAvaria;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoricoProduto other = (HistoricoProduto) obj;
		return Objects.equals(id, other.id);
	}
}
