package br.com.sis.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import br.com.sis.enuns.StatusVeiculo;
import br.com.sis.enuns.TipoVeiculo;

@Entity
@Table(name = "veiculos")
public class Veiculo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String placa;
	private String chassi;
	private String renavan;
	private String cor;
	private String foto;
	private Integer quilometragem;
	private BigDecimal valorCompra;
	private BigDecimal valorVenda;
	private TipoVeiculo tipoVeiculo;
	private List<Acessorio> acessorios = new ArrayList<Acessorio>();
	private List<Manutencao> manutencoes = new ArrayList<Manutencao>();
	private Integer anoFabricacao;
	private Integer anoModelo;
	private Modelo modelo;
	private StatusVeiculo statusVeiculo;
	private boolean veiculoDeParceiro = false;
	private List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();
	private String combustivel;
	private boolean veiculoAluguel = false;
	private BigDecimal valorAluguel;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 20)
	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	@NotBlank
	@Column(length = 40, nullable = false)
	public String getChassi() {
		return chassi;
	}

	public void setChassi(String chassi) {
		this.chassi = chassi;
	}

	@Column(length = 20)
	public String getRenavan() {
		return renavan;
	}

	public void setRenavan(String renavan) {
		this.renavan = renavan;
	}

	@Column(length = 20)
	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	@Column(length = 30)
	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Integer getQuilometragem() {
		return quilometragem;
	}

	public void setQuilometragem(Integer quilometragem) {
		this.quilometragem = quilometragem;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getValorCompra() {
		return valorCompra;
	}

	public void setValorCompra(BigDecimal valorCompra) {
		this.valorCompra = valorCompra;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}

	@Enumerated(EnumType.ORDINAL)
	public TipoVeiculo getTipoVeiculo() {
		return tipoVeiculo;
	}

	public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}

	@ManyToMany
	@JoinTable(name = "veiculo_acessorio", joinColumns = @JoinColumn(name = "veiculo_id"), inverseJoinColumns = @JoinColumn(name = "acessorio_id"))
	public List<Acessorio> getAcessorios() {
		return acessorios;
	}

	public void setAcessorios(List<Acessorio> acessorios) {
		this.acessorios = acessorios;
	}

	@OneToMany(mappedBy = "veiculo")
	public List<Manutencao> getManutencoes() {
		return manutencoes;
	}

	public void setManutencoes(List<Manutencao> manutencoes) {
		this.manutencoes = manutencoes;
	}

	public Integer getAnoFabricacao() {
		return anoFabricacao;
	}

	public void setAnoFabricacao(Integer anoFabricacao) {
		this.anoFabricacao = anoFabricacao;
	}

	public Integer getAnoModelo() {
		return anoModelo;
	}

	public void setAnoModelo(Integer anoModelo) {
		this.anoModelo = anoModelo;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "modelo_id", nullable = false)
	public Modelo getModelo() {
		return modelo;
	}

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

	@Enumerated(EnumType.ORDINAL)
	public StatusVeiculo getStatusVeiculo() {
		return statusVeiculo;
	}

	public void setStatusVeiculo(StatusVeiculo statusVeiculo) {
		this.statusVeiculo = statusVeiculo;
	}

	public boolean isVeiculoDeParceiro() {
		return veiculoDeParceiro;
	}

	public void setVeiculoDeParceiro(boolean veiculoDeParceiro) {
		this.veiculoDeParceiro = veiculoDeParceiro;
	}

	@OneToMany(mappedBy = "veiculo", orphanRemoval = true)
	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	@NotBlank
	@Column(length = 20, nullable = false)
	public String getCombustivel() {
		return combustivel;
	}

	public void setCombustivel(String combustivel) {
		this.combustivel = combustivel;
	}

	public boolean isVeiculoAluguel() {
		return veiculoAluguel;
	}

	public void setVeiculoAluguel(boolean veiculoAluguel) {
		this.veiculoAluguel = veiculoAluguel;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getValorAluguel() {
		return valorAluguel;
	}

	public void setValorAluguel(BigDecimal valorAluguel) {
		this.valorAluguel = valorAluguel;
	}

	@Transient
	public boolean isNoPatio() {
		return this.statusVeiculo.equals(StatusVeiculo.PATIO);
	}
	
	@Transient
	public boolean isParaAluguel() {
		return this.statusVeiculo.equals(StatusVeiculo.PARA_ALUGUEL);
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
		Veiculo other = (Veiculo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
