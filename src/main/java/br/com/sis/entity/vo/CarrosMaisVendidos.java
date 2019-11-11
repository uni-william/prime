package br.com.sis.entity.vo;

import java.io.Serializable;

import br.com.sis.entity.Modelo;

public class CarrosMaisVendidos implements Serializable {

	private static final long serialVersionUID = 1L;

	private Modelo modelo;
	private Long qtd;

	public Modelo getModelo() {
		return modelo;
	}

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

	public Long getQtd() {
		return qtd;
	}

	public void setQtd(Long qtd) {
		this.qtd = qtd;
	}

	public CarrosMaisVendidos() {
	}

	public CarrosMaisVendidos(Modelo modelo, Long qtd) {
		this.modelo = modelo;
		this.qtd = qtd;
	}

}
