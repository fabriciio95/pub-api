package com.pub.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.pub.domain.model.enums.StatusPromocao;
import com.pub.domain.model.enums.TipoRegraPromocao;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class RegraPromocao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private StatusPromocao status;
	
	private BigDecimal meta;
	
	private BigDecimal valorRegra;
	
	@Enumerated(EnumType.STRING)
	private TipoRegraPromocao tipoRegra;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "produto_gratis_id", nullable = true)
	private Produto produtoGratis;
	
	@CreationTimestamp
	private OffsetDateTime dataCadastro;
	
	@UpdateTimestamp
	private OffsetDateTime dataAtualizacao;
	
	@ManyToOne
	@JoinColumn(name = "promocao_id")
	private Promocao promocao;
	
	@ManyToMany
	@JoinTable(name = "regra_promocao_produto",
	           joinColumns = @JoinColumn(name = "regra_id"),
	           inverseJoinColumns = @JoinColumn(name = "produto_id"))
	private Set<Produto> produtos = new HashSet<>();
	
	public void adicionarProduto(Produto produto) {
		this.produtos.add(produto);
	}
	
	public void removerProduto(Produto produto) {
		this.produtos.remove(produto);
	}
}
