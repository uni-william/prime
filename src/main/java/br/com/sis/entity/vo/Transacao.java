package br.com.sis.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Veiculo;
import br.com.sis.util.Utils;

public class Transacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date data;
	private BigDecimal valor;
	private BigDecimal entrada;
	private Pessoa cliente = new Pessoa();
	private Veiculo veiculo = new Veiculo();
	private String operacao;
	private String parceiro;

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

	public BigDecimal getEntrada() {
		return entrada;
	}

	public void setEntrada(BigDecimal entrada) {
		this.entrada = entrada;
	}

	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public String getParceiro() {
		return parceiro;
	}

	public void setParceiro(String parceiro) {
		this.parceiro = parceiro;
	}
	
	public String getDataFormatadaBr() {
		return(Utils.dataCompletaFormatada(this.data));
	}
	
	public String getValorFormatadoBr() {
		if (this.valor == null) {
			return null;
		}
		return (Utils.valorFormatadoBR(this.valor));
	}

	public String getEntradaFormatadoBr() {
		if (this.entrada == null) {
			return null;
		}
		return (Utils.valorFormatadoBR(this.entrada));
	}
	
}
