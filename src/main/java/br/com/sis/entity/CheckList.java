package br.com.sis.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "checklist_contrato")
public class CheckList implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Aluguel aluguel;
	private ItemCheckList itemCheckList;
	private BigDecimal quantidade;
	private boolean entrega;
	private boolean recebimento;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "aluguel_id", nullable = false)
	public Aluguel getAluguel() {
		return aluguel;
	}

	public void setAluguel(Aluguel aluguel) {
		this.aluguel = aluguel;
	}

	@OneToOne
	@JoinColumn(name = "item_checklist_id", nullable = false)
	public ItemCheckList getItemCheckList() {
		return itemCheckList;
	}

	public void setItemCheckList(ItemCheckList itemCheckList) {
		this.itemCheckList = itemCheckList;
	}

	@Column(precision = 10, scale = 0)
	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public boolean isEntrega() {
		return entrega;
	}

	public void setEntrega(boolean entrega) {
		this.entrega = entrega;
	}

	public boolean isRecebimento() {
		return recebimento;
	}

	public void setRecebimento(boolean recebimento) {
		this.recebimento = recebimento;
	}
	
	
	@Transient
	public BigDecimal getValorItemCheckList() {
		if (this.recebimento == true && this.entrega == false) {
			return this.quantidade.multiply(this.itemCheckList.getValor());
		} else {
			return BigDecimal.ZERO;
		}
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
		CheckList other = (CheckList) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
