package br.com.sis.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.com.sis.enuns.StatusAluguel;

@Entity
@Table(name = "alugueis")
public class Aluguel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Pessoa cliente;
	private Pessoa funcionario;
	private Veiculo veiculo;
	private Date dataInicio;
	private Date dataPrevista;
	private Date dataEntrega;
	private BigDecimal valorDiaria;
	private BigDecimal valorDesconto;
	private BigDecimal valorAcrescimo;
	private BigDecimal valorTotal;
	private BigDecimal valorLuva;
	private StatusAluguel statusAluguel;
	private Integer kmInicial;
	private Integer kmFinal;
	private Integer kmReal;
	private List<CheckList> checkList = new ArrayList<CheckList>();
	private boolean pagamentoSemanal = false;
	private Date dataProximoPagamento;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_id", nullable = false)
	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
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
	@Temporal(TemporalType.DATE)
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	public Date getDataPrevista() {
		return dataPrevista;
	}

	public void setDataPrevista(Date dataPrevista) {
		this.dataPrevista = dataPrevista;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getValorDiaria() {
		return valorDiaria;
	}

	public void setValorDiaria(BigDecimal valorDiaria) {
		this.valorDiaria = valorDiaria;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getValorAcrescimo() {
		return valorAcrescimo;
	}

	public void setValorAcrescimo(BigDecimal valorAcrescimo) {
		this.valorAcrescimo = valorAcrescimo;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getValorLuva() {
		return valorLuva;
	}

	public void setValorLuva(BigDecimal valorLuva) {
		this.valorLuva = valorLuva;
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	public StatusAluguel getStatusAluguel() {
		return statusAluguel;
	}

	public void setStatusAluguel(StatusAluguel statusAluguel) {
		this.statusAluguel = statusAluguel;
	}

	@OneToMany(mappedBy = "aluguel", cascade = CascadeType.ALL)
	public List<CheckList> getCheckList() {
		return checkList;
	}

	public void setCheckList(List<CheckList> checkList) {
		this.checkList = checkList;
	}

	@NotNull
	public Integer getKmInicial() {
		return kmInicial;
	}

	public void setKmInicial(Integer kmInicial) {
		this.kmInicial = kmInicial;
	}

	public Integer getKmFinal() {
		return kmFinal;
	}

	public void setKmFinal(Integer kmFinal) {
		this.kmFinal = kmFinal;
	}

	public Integer getKmReal() {
		return kmReal;
	}

	public void setKmReal(Integer kmReal) {
		this.kmReal = kmReal;
	}

	public boolean isPagamentoSemanal() {
		return pagamentoSemanal;
	}

	public void setPagamentoSemanal(boolean pagamentoSemanal) {
		this.pagamentoSemanal = pagamentoSemanal;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataProximoPagamento() {
		return dataProximoPagamento;
	}

	public void setDataProximoPagamento(Date dataProximoPagamento) {
		this.dataProximoPagamento = dataProximoPagamento;
	}
	
	@Transient
	public String getDescricaoPagamentoSemanal() {
		return this.isPagamentoSemanal() ? "Sim" : "Não";
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
		Aluguel other = (Aluguel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
