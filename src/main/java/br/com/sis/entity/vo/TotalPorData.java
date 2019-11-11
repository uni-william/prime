package br.com.sis.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TotalPorData implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date data;
	private BigDecimal valor;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TotalPorData() {
	}

	public TotalPorData(Date data, BigDecimal valor) {
		this.data = data;
		this.valor = valor;
	}

}
