package br.com.sis.repository.filter;

import java.util.Date;

import br.com.sis.enuns.StatusAluguel;

public class AluguelFilter {

	private Date dtIni;
	private Date dtFim;
	private String documentoReceita;
	private String nome;
	private StatusAluguel statusAluguel;
	private Boolean pagtoSemanal;

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

	public String getDocumentoReceita() {
		return documentoReceita;
	}

	public void setDocumentoReceita(String documentoReceita) {
		this.documentoReceita = documentoReceita;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public StatusAluguel getStatusAluguel() {
		return statusAluguel;
	}

	public void setStatusAluguel(StatusAluguel statusAluguel) {
		this.statusAluguel = statusAluguel;
	}

	public Boolean getPagtoSemanal() {
		return pagtoSemanal;
	}

	public void setPagtoSemanal(Boolean pagtoSemanal) {
		this.pagtoSemanal = pagtoSemanal;
	}

}
