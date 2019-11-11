package br.com.sis.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.com.sis.enuns.StatusVenda;

@Entity
@Table(name = "intermediacoes")
public class Intermediacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date data;
	private BigDecimal valorFinanciado;
	private Pessoa cliente;
	private Veiculo veiculo;
	private Pessoa parceiro;
	private Pessoa funcionario;
	private Banco banco;
	private BigDecimal entrada;
	private StatusVenda statusVenda = StatusVenda.ANDAMENTO;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@NotNull
	@Column(precision = 10, scale = 2)
	public BigDecimal getValorFinanciado() {
		return valorFinanciado;
	}

	public void setValorFinanciado(BigDecimal valorFinanciado) {
		this.valorFinanciado = valorFinanciado;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "cliente_id", nullable = false)
	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "veiculo_id", nullable = false)
	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parc_id", nullable = false)
	public Pessoa getParceiro() {
		return parceiro;
	}

	public void setParceiro(Pessoa parceiro) {
		this.parceiro = parceiro;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "func_id", nullable = false)
	public Pessoa getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}

	@ManyToOne
	@JoinColumn(name = "banco_id")
	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getEntrada() {
		return entrada;
	}

	public void setEntrada(BigDecimal entrada) {
		this.entrada = entrada;
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	public StatusVenda getStatusVenda() {
		return statusVenda;
	}

	public void setStatusVenda(StatusVenda statusVenda) {
		this.statusVenda = statusVenda;
	}
	
	@Transient
	public boolean isConcluida() {
		return this.statusVenda.equals(StatusVenda.CONCLUIDA);
	}
	
	@Transient
	public boolean isCancelada() {
		return this.statusVenda.equals(StatusVenda.CANCELADA);
	}
	
	@Transient
	public boolean isEmAndamento() {
		return this.statusVenda.equals(StatusVenda.ANDAMENTO);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Intermediacao other = (Intermediacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
