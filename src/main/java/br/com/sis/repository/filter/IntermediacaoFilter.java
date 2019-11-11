package br.com.sis.repository.filter;

import java.util.Date;

import br.com.sis.entity.Pessoa;

public class IntermediacaoFilter {

	private Date dtIni;
	private Date dtFim;
	private Pessoa parceiro;

	public Date getDtIni() {
		return dtIni;
	}

	public void setDtIni(Date dtIni) {
		this.dtIni = dtIni;
	}

	public Date getDtFim() {
		return dtFim;
	}

	public void setDtFim(Date dtFim) {
		this.dtFim = dtFim;
	}

	public Pessoa getParceiro() {
		return parceiro;
	}

	public void setParceiro(Pessoa parceiro) {
		this.parceiro = parceiro;
	}

}
