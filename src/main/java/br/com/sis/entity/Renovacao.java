package br.com.sis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "renovacoes")
public class Renovacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataInicioAnterior;
	private Date dataPrevistaAnterior;
	private Aluguel aluguel;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataInicioAnterior() {
		return dataInicioAnterior;
	}

	public void setDataInicioAnterior(Date dataInicioAnterior) {
		this.dataInicioAnterior = dataInicioAnterior;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataPrevistaAnterior() {
		return dataPrevistaAnterior;
	}

	public void setDataPrevistaAnterior(Date dataPrevistaAnterior) {
		this.dataPrevistaAnterior = dataPrevistaAnterior;
	}

	@ManyToOne
	@JoinColumn(name = "aluguel_id")
	public Aluguel getAluguel() {
		return aluguel;
	}

	public void setAluguel(Aluguel aluguel) {
		this.aluguel = aluguel;
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
		Renovacao other = (Renovacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
