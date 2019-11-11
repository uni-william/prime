package br.com.sis.repository.filter;

public class PessoaFilter {

	private String nome;
	private String documentoReceita;
	private String rg;
	private Boolean ativo;
	private Boolean inativo;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDocumentoReceita() {
		return documentoReceita;
	}

	public void setDocumentoReceita(String documentoReceita) {
		this.documentoReceita = documentoReceita;
	}

	public String getRg() {
		return rg;
	}

	public Boolean getInativo() {
		return inativo;
	}

	public void setInativo(Boolean inativo) {
		this.inativo = inativo;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

}
