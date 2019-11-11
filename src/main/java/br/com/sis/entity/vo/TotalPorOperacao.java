package br.com.sis.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.sis.enuns.TipoOperacao;

public class TotalPorOperacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private TipoOperacao tipoOperacao;
	private Long qtd;
	private BigDecimal valor;

	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

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

	public TotalPorOperacao() {
	}

	public TotalPorOperacao(TipoOperacao tipoOperacao, Long qtd, BigDecimal valor) {
		this.tipoOperacao = tipoOperacao;
		this.qtd = qtd;
		this.valor = valor;
	}

}
