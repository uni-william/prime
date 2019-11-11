package br.com.sis.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class TotalIntermediacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long qtd;
	private BigDecimal valor;

	public Long getQtd() {
		return qtd;
	}

	public void setQtd(Long qtd) {
		this.qtd = qtd;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public TotalIntermediacao() {
		
	}

	public TotalIntermediacao(Long qtd, BigDecimal valor) {
		this.qtd = qtd;
		this.valor = valor;
	}

}
