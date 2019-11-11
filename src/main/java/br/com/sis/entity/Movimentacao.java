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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.com.sis.enuns.StatusVenda;
import br.com.sis.enuns.TipoOperacao;

@Entity
@Table(name = "movimentacoes")
public class Movimentacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date data;
	private BigDecimal valor;
	private BigDecimal entrada;
	private TipoOperacao tipoOperacao;
	private Pessoa cliente;
	private Veiculo veiculo;
	private Pessoa funcionario;
	private byte[] termoAssinado;
	private Banco banco;
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
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getEntrada() {
		return entrada;
	}

	public void setEntrada(BigDecimal entrada) {
		this.entrada = entrada;
	}

	@Enumerated(EnumType.ORDINAL)
	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
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
	@JoinColumn(name = "func_id", nullable = false)
	public Pessoa getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}

	@Lob
	public byte[] getTermoAssinado() {
		return termoAssinado;
	}

	public void setTermoAssinado(byte[] termoAssinado) {
		this.termoAssinado = termoAssinado;
	}

	@ManyToOne
	@JoinColumn(name = "banco_id")
	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
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
	public boolean isPossuiTermoAssindo() {
		return this.termoAssinado != null;
	}

	@Transient
	public boolean isCompra() {
		return this.tipoOperacao.equals(TipoOperacao.COMPRA);
	}

	@Transient
	public boolean isVenda() {
		return this.tipoOperacao.equals(TipoOperacao.VENDA);
	}

	@Transient
	public BigDecimal getValorFinanciamento() {
		if (this.getValor() == null && this.getEntrada() == null) {
			return null;
		} else if (this.getValor() != null && this.getEntrada() == null) {
			return this.getValor();
		} else if (this.getValor() == null && this.getEntrada() != null) {
			return this.getEntrada().multiply(new BigDecimal(-1));
		} else {
			return this.getValor().subtract(this.getEntrada());
		}
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
		Movimentacao other = (Movimentacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
