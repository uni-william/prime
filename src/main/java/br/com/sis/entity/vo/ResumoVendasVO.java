package br.com.sis.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ResumoVendasVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date data;
	private String fabricante;
	private String modelo;
	private Long veiculoId;
	private String placa;
	private BigDecimal valorVenda;
	private BigDecimal valorCompra;
	private BigDecimal valorComissao;
	private BigDecimal valorManutencao;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Long getVeiculoId() {
		return veiculoId;
	}

	public void setVeiculoId(Long veiculoId) {
		this.veiculoId = veiculoId;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public BigDecimal getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}

	public BigDecimal getValorCompra() {
		return valorCompra;
	}

	public void setValorCompra(BigDecimal valorCompra) {
		this.valorCompra = valorCompra;
	}

	public BigDecimal getValorComissao() {
		return valorComissao;
	}

	public void setValorComissao(BigDecimal valorComissao) {
		this.valorComissao = valorComissao;
	}

	public BigDecimal getValorManutencao() {
		return valorManutencao;
	}

	public void setValorManutencao(BigDecimal valorManutencao) {
		this.valorManutencao = valorManutencao;
	}

	public BigDecimal getSaldo() {
		return this.getValorVenda().subtract(valorCompra != null ? valorCompra : BigDecimal.ZERO)
				.subtract(valorManutencao != null ? valorManutencao : BigDecimal.ZERO)
				.subtract(valorComissao != null ? valorComissao : BigDecimal.ZERO);
	}

	public ResumoVendasVO(Date data, String fabricante, String modelo, Long veiculoId, String placa,
			BigDecimal valorVenda, BigDecimal valorCompra, BigDecimal valorComissao) {
		this.data = data;
		this.fabricante = fabricante;
		this.modelo = modelo;
		this.veiculoId = veiculoId;
		this.placa = placa;
		this.valorVenda = valorVenda;
		this.valorCompra = valorCompra;
		this.valorComissao = valorComissao;
	}

	public ResumoVendasVO() {
	}

}
