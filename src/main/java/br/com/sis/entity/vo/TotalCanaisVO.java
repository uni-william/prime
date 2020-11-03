package br.com.sis.entity.vo;

import java.io.Serializable;

public class TotalCanaisVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String descricao;
	private Long total;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public TotalCanaisVO(String descricao, Long total) {
		this.descricao = descricao;
		this.total = total;
	}
	
	public TotalCanaisVO() {
	}

	
}
