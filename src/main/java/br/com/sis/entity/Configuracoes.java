package br.com.sis.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "configuracoes_email")
public class Configuracoes implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String usuarioEnviaEmail;
	private String senhaUsuarioEmail;
	private String emailEnvio;
	private String host;
	private String porta;
	private boolean sslOnConection;
	private boolean tlsRequired;
	private int diasRetroativosGraficoPizzaELinhas = 15;
	private int mesesRetroativosGrficoBarras = 12;
	private boolean enviarSenhaEmail = true;
	private Integer limiteKmAluguel = 7000;
	private BigDecimal valorKmExcedente;
	private Integer limiteGraficoVendas;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty
	@Column(length = 60, nullable = false)
	public String getUsuarioEnviaEmail() {
		return usuarioEnviaEmail;
	}

	public void setUsuarioEnviaEmail(String usuarioEnviaEmail) {
		this.usuarioEnviaEmail = usuarioEnviaEmail;
	}

	@NotEmpty
	@Column(length = 100, nullable = false)
	public String getSenhaUsuarioEmail() {
		return senhaUsuarioEmail;
	}

	public void setSenhaUsuarioEmail(String senhaUsuarioEmail) {
		this.senhaUsuarioEmail = senhaUsuarioEmail;
	}

	@NotEmpty
	@Column(length = 60, nullable = false)
	public String getEmailEnvio() {
		return emailEnvio;
	}

	public void setEmailEnvio(String emailEnvio) {
		this.emailEnvio = emailEnvio;
	}

	@NotEmpty
	@Column(length = 30, nullable = false)
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@NotEmpty
	@Column(length = 4)
	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public boolean isSslOnConection() {
		return sslOnConection;
	}

	public void setSslOnConection(boolean sslOnConection) {
		this.sslOnConection = sslOnConection;
	}

	public boolean isTlsRequired() {
		return tlsRequired;
	}

	public void setTlsRequired(boolean tlsRequired) {
		this.tlsRequired = tlsRequired;
	}

	public int getDiasRetroativosGraficoPizzaELinhas() {
		return diasRetroativosGraficoPizzaELinhas;
	}

	public void setDiasRetroativosGraficoPizzaELinhas(int diasRetroativosGraficoPizzaELinhas) {
		this.diasRetroativosGraficoPizzaELinhas = diasRetroativosGraficoPizzaELinhas;
	}

	public int getMesesRetroativosGrficoBarras() {
		return mesesRetroativosGrficoBarras;
	}

	public void setMesesRetroativosGrficoBarras(int mesesRetroativosGrficoBarras) {
		this.mesesRetroativosGrficoBarras = mesesRetroativosGrficoBarras;
	}

	public boolean isEnviarSenhaEmail() {
		return enviarSenhaEmail;
	}

	public void setEnviarSenhaEmail(boolean enviarSenhaEmail) {
		this.enviarSenhaEmail = enviarSenhaEmail;
	}

	public Integer getLimiteKmAluguel() {
		return limiteKmAluguel;
	}

	public void setLimiteKmAluguel(Integer limiteKmAluguel) {
		this.limiteKmAluguel = limiteKmAluguel;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getValorKmExcedente() {
		return valorKmExcedente;
	}

	public void setValorKmExcedente(BigDecimal valorKmExcedente) {
		this.valorKmExcedente = valorKmExcedente;
	}

	public Integer getLimiteGraficoVendas() {
		return limiteGraficoVendas;
	}

	public void setLimiteGraficoVendas(Integer limiteGraficoVendas) {
		this.limiteGraficoVendas = limiteGraficoVendas;
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
		Configuracoes other = (Configuracoes) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
